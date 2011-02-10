<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<jsp:useBean id="compareValidators" scope="session" class="fr.paris.lutece.plugins.form.modules.comparevalidators.web.CompareValidatorsJspBean" />

<% compareValidators.init( request, compareValidators.RIGHT_MANAGE_FORM ); %>
<%= compareValidators.getCreateRule( request ) %>

<%@ include file="../../../../AdminFooter.jsp" %>
