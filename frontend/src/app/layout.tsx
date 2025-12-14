import type { ReactNode } from 'react';
import '../styles/globals.css';

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="en">
      <body>
        <div className="container">
          <header className="header">
            <a href="/" className="brand">Nexus</a>
            <nav className="nav">
              <a href="/jobs">Jobs</a>
              <a href="/workers">Workers</a>
            </nav>
          </header>
          <main>{children}</main>
        </div>
      </body>
    </html>
  );
}
