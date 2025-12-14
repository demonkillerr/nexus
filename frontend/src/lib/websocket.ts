import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export function connectStomp(): Client {
  const url = process.env.NEXT_PUBLIC_SCHEDULER_WS_URL ?? 'http://localhost:8080/ws';
  const client = new Client({
    webSocketFactory: () => new SockJS(url),
    reconnectDelay: 1000
  });
  client.activate();
  return client;
}
