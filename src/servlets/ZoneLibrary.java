/*
 * servlet for displaying library list elements to be added to playlist
 */
package servlets;

import contrib.CIFSNetworkInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jcifs.smb.SmbFile;
import multicastmusiccontroller.ProgramConstants;
import zoneserver.ZoneServerUtility;

/**
 * @author Jason Zerbe
 */
public class ZoneLibrary extends HttpServlet implements ProgramConstants {

    public ZoneLibrary() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter(); //get the response writer for later
        String aPathStr = req.getParameter("path");
        if ((aPathStr != null) && (!aPathStr.equals(""))) { //make sure this contains a path
            if (aPathStr.contains("smb://")) { //CIFS share
                HashMap<SmbFile, String> aCIFSDirMap = CIFSNetworkInterface.getInstance().getDirectoryList(aPathStr);
                int i = 0;
                for (SmbFile iSmbFile : aCIFSDirMap.keySet()) {
                    out.println("<li id='directorylistitem_" + i + "'>");
                    if (aCIFSDirMap.get(iSmbFile).equals("")) { //this is a directory
                        out.println("<a href='javascript:browseDirectory(&quot;"
                                + iSmbFile.getPath() + "&quot;);'>"
                                + iSmbFile.getName() + "</a>");
                    } else {
                        out.println("<a href='javascript:addMediaToPlayList(&quot;"
                                + iSmbFile.getPath() + "&quot;);' data-role='button' data-icon='plus'>"
                                + iSmbFile.getName() + "</a>");
                    }
                    out.println("</li>");
                    i++;
                }
            }
        } else { //no path set, load various base preference directories
            List<String> rootPathStrList = ZoneServerUtility.getInstance().getMediaDirEntries();
            if (rootPathStrList.size() > 0) {
                int i = 0;
                for (String rootPathStr : rootPathStrList) {
                    out.println("<li id='directorylistitem_" + i + "'>");
                    out.println("<a href='javascript:browseDirectory(&quot;" + rootPathStr + "&quot;);'>" + rootPathStr + "</a>");
                    out.println("</li>");
                    i++;
                }
            }
        }
    }
}