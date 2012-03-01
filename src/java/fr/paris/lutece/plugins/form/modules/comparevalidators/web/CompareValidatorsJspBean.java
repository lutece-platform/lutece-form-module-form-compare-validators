/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.form.modules.comparevalidators.web;

import fr.paris.lutece.plugins.form.modules.comparevalidators.business.OperatorHome;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.Rule;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.RuleHome;
import fr.paris.lutece.plugins.form.modules.comparevalidators.service.CompareValidatorsPlugin;
import fr.paris.lutece.plugins.form.modules.comparevalidators.service.CompareValidatorsResourceIdService;
import fr.paris.lutece.plugins.form.modules.comparevalidators.service.CompareValidatorsService;
import fr.paris.lutece.plugins.form.modules.comparevalidators.util.CompareValidatorsConstants;
import fr.paris.lutece.plugins.form.modules.comparevalidators.util.CompareValidatorsUtils;
import fr.paris.lutece.plugins.form.web.FormJspBean;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * CompareValidatorsJspBean
 */
public class CompareValidatorsJspBean extends FormJspBean
{
    // Parameters
    private static final String PARAMETER_ID_FORM = "id_form";
    private static final String PARAMETER_ID_RULE = "id_rule";
    private static final String PARAMETER_ID_ENTRY1 = "id_entry1";
    private static final String PARAMETER_ID_ENTRY2 = "id_entry2";
    private static final String PARAMETER_ID_OPERATOR = "id_operator";
    private static final String PARAMETER_CANCEL = "cancel";

    // I18n
    private static final String PROPERTY_CREATE_RULE_TITLE = "module.form.comparevalidators.createRule.title";
    private static final String FIELD_ENTRY1 = "module.form.comparevalidators.createRule.labelEntry1";
    private static final String FIELD_ENTRY2 = "module.form.comparevalidators.createRule.labelEntry2";
    private static final String FIELD_OPERATOR = "module.form.comparevalidators.createRule.labelOperator";
    private static final String MESSAGE_MANDATORY_FIELD = "module.form.comparevalidators.message.mandatory.field";
    private static final String MESSAGE_ERROR_IDENTICAL_ENTRIES = "module.form.comparevalidators.message.errorIdenticalEntries";
    private static final String MESSAGE_ERROR_RULE_ALREADY_EXISTS = "module.form.comparevalidators.message.errorRuleAlreadyExists";
    private static final String MESSAGE_CONFIRM_REMOVE_RULE = "module.form.comparevalidators.message.confirmRemoveRule";

    // Template
    private static final String TEMPLATE_CREATE_RULE = "admin/plugins/form/modules/comparevalidators/create_rule.html";

    // JSP
    private static final String JSP_DO_REMOVE_RULE = "jsp/admin/plugins/form/modules/comparevalidators/DoRemoveRule.jsp";

    // Plugin
    private static final Plugin PLUGIN = PluginService.getPlugin( CompareValidatorsPlugin.PLUGIN_NAME );

    /**
     * Gets the rule creation page
     *
     * @param request The HTTP request
     * @return The rule creation page
     */
    public String getCreateRule( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( Rule.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    CompareValidatorsResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            return getManageValidator( request );
        }

        setPageTitleProperty( PROPERTY_CREATE_RULE_TITLE );

        int nIdForm = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_FORM ) );

        ReferenceList refListOperator = OperatorHome.findAll( PLUGIN );
        ReferenceItem refItem = new ReferenceItem(  );
        refItem.setCode( String.valueOf( -1 ) );
        refItem.setName( CompareValidatorsConstants.EMPTY_STRING );
        refListOperator.add( 0, refItem );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( CompareValidatorsConstants.MARK_ID_FORM, nIdForm );
        model.put( CompareValidatorsConstants.MARK_ENTRY_LIST, CompareValidatorsService.getAuthorizedEntries( nIdForm ) );
        model.put( CompareValidatorsConstants.MARK_OPERATOR_LIST, refListOperator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Performs the rule creation
     *
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doCreateRule( HttpServletRequest request )
    {
        int nIdForm = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_FORM ) );

        if ( !RBACService.isAuthorized( Rule.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    CompareValidatorsResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            return getJspManageValidator( request, nIdForm );
        }

        // Cancel
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_CANCEL ) ) )
        {
            return getJspManageValidator( request, nIdForm );
        }

        Rule rule = new Rule(  );

        // Get fields
        String strError = getFields( request, nIdForm, rule );

        if ( strError != null )
        {
            return strError;
        }

        RuleHome.create( rule, PLUGIN );

        return getJspManageValidator( request, nIdForm );
    }

    /**
     * Gets the form fields
     *
     * @param request The HTTP request
     * @param nIdForm The form identifier
     * @param rule The rule
     * @return Null if no error, else the AdminMessage URL
     */
    private String getFields( HttpServletRequest request, int nIdForm, Rule rule )
    {
        int nIdEntry1 = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_ENTRY1 ) );
        int nIdEntry2 = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_ENTRY2 ) );
        int nIdOperator = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_OPERATOR ) );

        String strFieldError = CompareValidatorsConstants.EMPTY_STRING;

        if ( nIdEntry1 == -1 )
        {
            strFieldError = FIELD_ENTRY1;
        }
        else if ( nIdOperator == -1 )
        {
            strFieldError = FIELD_OPERATOR;
        }
        else if ( nIdEntry2 == -1 )
        {
            strFieldError = FIELD_ENTRY2;
        }

        if ( !strFieldError.equals( CompareValidatorsConstants.EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strFieldError, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields,
                AdminMessage.TYPE_STOP );
        }

        // Entries may not be identical
        if ( nIdEntry1 == nIdEntry2 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_IDENTICAL_ENTRIES, AdminMessage.TYPE_STOP );
        }

        // Checks if a rule already exists for these entries
        for ( Rule currentRule : RuleHome.findRulesByForm( nIdForm, PLUGIN ) )
        {
            if ( ( ( currentRule.getIdEntry1(  ) == nIdEntry1 ) && ( currentRule.getIdEntry2(  ) == nIdEntry2 ) ) ||
                    ( ( currentRule.getIdEntry1(  ) == nIdEntry2 ) && ( currentRule.getIdEntry2(  ) == nIdEntry1 ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_RULE_ALREADY_EXISTS,
                    AdminMessage.TYPE_STOP );
            }
        }

        rule.setIdForm( nIdForm );
        rule.setIdEntry1( nIdEntry1 );
        rule.setIdEntry2( nIdEntry2 );
        rule.setIdOperator( nIdOperator );

        // No error
        return null;
    }

    /**
     * Gets the confirmation page for removing rule
     * @param request The HTTP request
     * @return The confirmation page for removing rule
     */
    public String getConfirmRemoveRule( HttpServletRequest request )
    {
        int nIdForm = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_FORM ) );

        if ( !RBACService.isAuthorized( Rule.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    CompareValidatorsResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return getJspManageValidator( request, nIdForm );
        }

        int nIdRule = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_RULE ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_RULE );
        url.addParameter( PARAMETER_ID_FORM, nIdForm );
        url.addParameter( PARAMETER_ID_RULE, nIdRule );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_RULE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Performs the rule removal
     *
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doRemoveRule( HttpServletRequest request )
    {
        int nIdForm = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_FORM ) );

        if ( !RBACService.isAuthorized( Rule.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    CompareValidatorsResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return getJspManageValidator( request, nIdForm );
        }

        int nIdRule = CompareValidatorsUtils.stringToInt( request.getParameter( PARAMETER_ID_RULE ) );
        RuleHome.remove( nIdRule, PLUGIN );

        return getJspManageValidator( request, nIdForm );
    }
}
