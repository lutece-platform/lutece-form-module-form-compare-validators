<%@ page errorPage="../../../../ErrorPage.jsp" %>

<jsp:useBean id="compareValidators" scope="session" class="fr.paris.lutece.plugins.form.modules.comparevalidators.web.CompareValidatorsJspBean" />

<% 
	compareValidators.init( request, compareValidators.RIGHT_MANAGE_FORM );
    response.sendRedirect( compareValidators.getConfirmRemoveRule( request ) );
%>
