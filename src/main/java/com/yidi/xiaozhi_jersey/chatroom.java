
package com.yidi.xiaozhi_jersey;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.yidi.entity.TestInfoItem;
import com.yidi.service.MainService;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/chatroom")
public class chatroom {


    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void getIt(@Context HttpServletRequest request,@Context HttpServletResponse response) {
    	try {
			request.getRequestDispatcher("/chatroom.html").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Path("sendtext")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_JSON})
	public String getreply(@FormParam("inputtext") String text) throws SQLException {
    	Gson gson=new Gson();
    	MainService main=new MainService("test", "", text);
    	String teString=main.getReply();
    	if(main.getReply().equals("")) {
    		TestInfoItem instance=new TestInfoItem(main.getSenderid(),"api");
        	return gson.toJson(instance);
    	}
    	TestInfoItem instance=new TestInfoItem(main.getSenderid(), main.getReply());
    	return gson.toJson(instance);
    }

}
