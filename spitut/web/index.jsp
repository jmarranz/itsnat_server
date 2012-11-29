<%
    response.addDateHeader("Expires", 0);
    response.addHeader("Cache-Control", "no-store,no-cache,must-revalidate,post-check=0,pre-check=0");
%>

<jsp:forward page="/servlet" />
