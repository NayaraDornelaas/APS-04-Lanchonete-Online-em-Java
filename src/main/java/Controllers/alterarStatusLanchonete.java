package Controllers;

import DAO.DaoStatusLanchonete;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class alterarStatusLanchonete extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Lê o corpo da requisição
        BufferedReader br = request.getReader();
        String json = br.readLine();
        
        if (json != null) {
            JSONObject dados = new JSONObject(json);
            String novoStatus = dados.getString("status");
            
            if (novoStatus != null && (novoStatus.equals("ABERTO") || novoStatus.equals("FECHADO"))) {
                DaoStatusLanchonete dao = new DaoStatusLanchonete();
                dao.alterarStatus(novoStatus);
                
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", novoStatus);
                
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse.toString());
                    out.flush();
                }
            } else {
                // Definir o status inicial como 'ABERTO' se o status for inválido
                DaoStatusLanchonete dao = new DaoStatusLanchonete();
                dao.alterarStatus("ABERTO");
                
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ABERTO");
                
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse.toString());
                    out.flush();
                }
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("Status inválido");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
} 