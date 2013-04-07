package com.haka;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.JsonObject;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Dashboard() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();

		response.setContentType("text/html");

		FacebookClient facebookClient = new DefaultFacebookClient(
				"AAACEdEose0cBAEGWchpW2xX8ACZAPE6uHNSnBKD9ypQTPJ7tZCJrcBhuO0K4LGpHQqEk0s360VYAuZAMqJH9PhiZBhLHFSIU8lBoITxwu6ayIfLI32n5");

		String query = "SELECT pid,src_big FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner=me())";
		List<JsonObject> queryResults = facebookClient.executeFqlQuery(query,
				JsonObject.class);

		ServletContext cntx = getServletContext();

		for (JsonObject jsonObj : queryResults) {
			try
			{
			URL url = new URL(jsonObj.getString("src_big"));
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] responseOut = out.toByteArray();

			FileOutputStream fos = new FileOutputStream(
					cntx.getRealPath(jsonObj.getString("pid") + ".jpg"));
			fos.write(responseOut);
			fos.close();

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"https://autotags.iqengines.com/api/v0.1/photo/");

			FileBody file = new FileBody(new File(cntx.getRealPath(jsonObj
					.getString("pid")+ ".jpg")));
			StringBody key = new StringBody("50baa58e64d34b9cb014fde01bf8758a");

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", file);
			reqEntity.addPart("key", key);
			httppost.setEntity(reqEntity);

			HttpResponse response1 = httpclient.execute(httppost);
			HttpEntity resEntity = response1.getEntity();
			JsonObject job = new JsonObject(resEntity.getContent());
			System.out.println("Yahoo....!"+job.getString("tags"));
			
			}catch(Exception e)
			{
				System.out.println("Error");
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
