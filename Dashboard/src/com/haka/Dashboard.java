package com.haka;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
				"AAACEdEose0cBACpN1QhdWNW9FTHxBMyuOpiuRUtIyeGDHmOjty8ZA7b1sQag7hCEnjZCCm3ZBDOIniGX2xqgeBXYmd975VCZC5EKKsDsxMiZBs4n4cjMr");

		String query = "SELECT pid,src_big FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner=me())";
		List<JsonObject> queryResults = facebookClient.executeFqlQuery(query,
				JsonObject.class);

		ServletContext cntx = getServletContext();

		Iterator itr = queryResults.iterator();

		while (itr.hasNext()) {
			try {
				JsonObject jsonObj = (JsonObject) itr.next();
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
						.getString("pid") + ".jpg")));
				StringBody key = new StringBody(
						"50baa58e64d34b9cb014fde01bf8758a");

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("file", file);
				reqEntity.addPart("key", key);
				httppost.setEntity(reqEntity);

				HttpResponse response1 = httpclient.execute(httppost);
				HttpEntity resEntity = response1.getEntity();

				Scanner s = new Scanner(resEntity.getContent());
				StringBuilder builder = new StringBuilder();
				while (s.hasNextLine()) {
					builder.append(s.nextLine() + "\n");
				}

				pw.write(builder.toString());

			} catch (Exception e) {
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
