package dexbot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import dexbot.repository.Repository;

public class TemplateServlet extends HttpServlet {
	private static final long serialVersionUID = -5415151991071701476L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String parameter = req.getParameter("id_key");
		if (parameter != null) {
			Long idKey = Long.parseLong(parameter);
			Repository repository = new Repository();
			Key baseKey = KeyFactory.createKey("template_base", idKey);
			String merged = repository.mergeEmail(baseKey);
			resp.setContentType("text/html;charset=UTF-8");
			resp.getOutputStream().write(merged.getBytes("UTF-8"));
		}
	}
}
