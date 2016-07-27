package handler;

import result.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {

    Result processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException;
}
