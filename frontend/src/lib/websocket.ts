import { Client, IMessage } from '@stomp/stompjs';

let stompClient: Client | null = null;
let connectionPromise: Promise<Client> | null = null;

export function getStompClient(): Promise<Client> {
  if (connectionPromise) {
    return connectionPromise;
  }

  connectionPromise = new Promise((resolve, reject) => {
    const url = process.env.NEXT_PUBLIC_SCHEDULER_WS_URL ?? 'http://localhost:8080/ws';

    stompClient = new Client({
      webSocketFactory: () => {
        // Dynamic import to avoid SSR issues
        // eslint-disable-next-line @typescript-eslint/no-require-imports
        const SockJS = require('sockjs-client');
        return new SockJS(url);
      },
      reconnectDelay: 1000,
      onConnect: () => {
        resolve(stompClient!);
      },
      onStompError: (frame) => {
        reject(new Error(`STOMP error: ${frame.headers['message']}`));
      }
    });
    stompClient.activate();
  });

  return connectionPromise;
}

export function disconnectStomp(): void {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
    connectionPromise = null;
  }
}

export async function subscribeToTopic(
  topic: string,
  callback: (message: IMessage) => void
): Promise<{ unsubscribe: () => void }> {
  const client = await getStompClient();
  const subscription = client.subscribe(topic, callback);
  return {
    unsubscribe: () => subscription.unsubscribe()
  };
}
