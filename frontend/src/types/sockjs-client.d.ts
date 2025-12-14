declare module 'sockjs-client' {
  class SockJS {
    constructor(url: string, _reserved?: any, options?: { transports?: string[] });
    close(): void;
    send(data: string): void;
    onopen: (() => void) | null;
    onclose: ((ev: any) => void) | null;
    onmessage: ((ev: { data: string }) => void) | null;
    onerror: ((ev: any) => void) | null;
  }
  export = SockJS;
}
