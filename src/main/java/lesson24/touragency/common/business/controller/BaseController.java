package lesson24.touragency.common.business.controller;

import lesson24.touragency.common.business.search.Paginator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseController extends HttpServlet {
    protected void forwardToPage(HttpServletRequest req, HttpServletResponse resp, String dest) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/" + dest);
        dispatcher.forward(req, resp);
    }

    protected boolean isParamExists(HttpServletRequest request, String paramName) {
        return request.getParameterMap().containsKey(paramName);
    }

    protected Paginator createPaginatorFromRequest(HttpServletRequest request) {
        Paginator paginator = new Paginator();

        if (isParamExists(request, "limit")) {
            paginator.setLimit(Integer.parseInt(request.getParameter("limit")));
        }

        if (isParamExists(request, "offset")) {
            paginator.setOffset(Integer.parseInt(request.getParameter("offset")));
        }

        return paginator;

    }


    protected void redirectTo404(String message, HttpServletResponse servletResponse) {

    }

    protected void redirectTo500(String message, HttpServletResponse response) {
        try {
            response.sendRedirect("error500?errorMessage=" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
