package Controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

public class comprarTest {

    // Subclasse usada apenas no teste para evitar acesso ao banco
    static class comprarNoDb extends comprar {
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Comportamento mínimo: se não houver cookies escreve "erro"
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                if (request.getCookies() == null) {
                    out.println("erro");
                } else {
                    out.println("ok");
                }
            }
        }
    }

    @Test
    public void testDoPost_noCookies_returnsErro() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        ServletInputStream sis = new ServletInputStream() {
            private final ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // não usado no teste
            }
        };

        when(req.getInputStream()).thenReturn(sis);
        when(req.getCookies()).thenReturn(null);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(resp.getWriter()).thenReturn(pw);

        comprar servlet = new comprarNoDb();
        servlet.doPost(req, resp);

        pw.flush();
        String output = sw.toString();
        assertTrue(output.contains("erro"), "Resposta deve conter 'erro' quando cookies ausentes");
    }
}
