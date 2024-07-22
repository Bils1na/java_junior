package org.example.sem5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hw5.BroadcastMessageRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    try (ServerSocket server = new ServerSocket(8888)) {
      System.out.println("Сервер запущен");

      while (true) {
        System.out.println("Ждем клиентского подключения");
        Socket client = server.accept();
        ClientHandler clientHandler = new ClientHandler(client, clients);
        new Thread(clientHandler).start();
      }
    } catch (IOException e) {
      System.err.println("Ошибка во время работы сервера: " + e.getMessage());
    }
  }

  private static class ClientHandler implements Runnable {

    private final Socket client;
    private final Scanner in;
    private final PrintWriter out;
    private final Map<String, ClientHandler> clients;
    private String clientLogin;

    public ClientHandler(Socket client, Map<String, ClientHandler> clients) throws IOException {
      this.client = client;
      this.clients = clients;

      this.in = new Scanner(client.getInputStream());
      this.out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
      System.out.println("Подключен новый клиент");

      try {
        String loginRequest = in.nextLine();
        LoginRequest request = objectMapper.reader().readValue(loginRequest, LoginRequest.class);
        this.clientLogin = request.getLogin();
      } catch (IOException e) {
        System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
        String unsuccessfulResponse = createLoginResponse(false);
        out.println(unsuccessfulResponse);
        doClose();
        return;
      }

      System.out.println("Запрос от клиента: " + clientLogin);
      // Проверка, что логин не занят
      if (clients.containsKey(clientLogin)) {
        String unsuccessfulResponse = createLoginResponse(false);
        out.println(unsuccessfulResponse);
        doClose();
        return;
      }

      clients.put(clientLogin, this);
      String successfulLoginResponse = createLoginResponse(true);
      out.println(successfulLoginResponse);

      while (true) {
        String msgFromClient = in.nextLine();

        final String type;
        try {
          AbstractRequest request = objectMapper.reader().readValue(msgFromClient, AbstractRequest.class);
          type = request.getType();
        } catch (IOException e) {
          System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
          sendMessage("Не удалось прочитать сообщение: " + e.getMessage());
          continue;
        }

        if (SendMessageRequest.TYPE.equals(type)) {
          // Клиент прислал SendMessageRequest

          final SendMessageRequest request;
          try {
            request = objectMapper.reader().readValue(msgFromClient, SendMessageRequest.class);
          } catch (IOException e) {
            System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
            sendMessage("Не удалось прочитать сообщение SendMessageRequest: " + e.getMessage());
            continue;
          }

          ClientHandler clientTo = clients.get(request.getRecipient());
          if (clientTo == null) {
            sendMessage("Клиент с логином [" + request.getRecipient() + "] не найден");
            continue;
          }
          clientTo.sendMessage(request.getMessage());
        } else if (BroadcastMessageRequest.TYPE.equals(type)) {
          // Клиент прислал BroadcastMessageRequest

          final BroadcastMessageRequest request;
          try {
            request = objectMapper.reader().readValue(msgFromClient, BroadcastMessageRequest.class);
          } catch (IOException e) {
            System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
            sendMessage("Не удалось прочитать сообщение BroadcastMessageRequest: " + e.getMessage());
            continue;
          }

          String sender = request.getSender();
          String message = request.getMessage();

          for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            String username = entry.getKey();
            if (!username.equals(sender)) {
              entry.getValue().sendMessage(message);
            }
          }
        } else if (DisconnectRequest.TYPE.equals(type)) {
          // Клиент прислал DisconnectRequest
          break;
        } else {
          System.err.println("Неизвестный тип сообщения: " + type);
          sendMessage("Неизвестный тип сообщения: " + type);
          continue;
        }
      }

      doClose();
    }

    private void doClose() {
      clients.remove(clientLogin);  // Удаляем клиента из списка
      try {
        in.close();
        out.close();
        client.close();
      } catch (IOException e) {
        System.err.println("Ошибка во время отключения клиента: " + e.getMessage());
      }

      // Рассылка другим клиентам об отключении
      for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
        entry.getValue().sendMessage("Клиент [" + clientLogin + "] отключился");
      }
    }

    public void sendMessage(String message) {
      out.println(message);
    }

    private String createLoginResponse(boolean success) {
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setConnected(success);
      try {
        return objectMapper.writer().writeValueAsString(loginResponse);
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Не удалось создать loginResponse: " + e.getMessage());
      }
    }
  }
}
