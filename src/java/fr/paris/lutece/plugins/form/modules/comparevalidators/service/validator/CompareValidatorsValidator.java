/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.form.modules.comparevalidators.service.validator;

import fr.paris.lutece.plugins.form.business.EntryHome;
import fr.paris.lutece.plugins.form.business.FormSubmit;
import fr.paris.lutece.plugins.form.business.IEntry;
import fr.paris.lutece.plugins.form.business.Response;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.Operator;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.OperatorHome;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.Rule;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.RuleHome;
import fr.paris.lutece.plugins.form.modules.comparevalidators.business.comparator.IComparator;
import fr.paris.lutece.plugins.form.modules.comparevalidators.service.CompareValidatorsPlugin;
import fr.paris.lutece.plugins.form.modules.comparevalidators.service.CompareValidatorsResourceIdService;
import fr.paris.lutece.plugins.form.modules.comparevalidators.service.CompareValidatorsService;
import fr.paris.lutece.plugins.form.modules.comparevalidators.util.CompareValidatorsConstants;
import fr.paris.lutece.plugins.form.service.validator.Validator;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * CompareValidatorsValidator
 */
public class CompareValidatorsValidator extends Validator
{
    // Properties
    private static final String PROPERTY_ITEMS_PER_PAGE = "form-compare-validators.paginator.itemsPerPage";

    // Markers
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_RULE_LIST = "rule_list";
    private static final String MARK_PERMISSION_CREATE = "permission_create";
    private static final String MARK_PERMISSION_DELETE = "permission_delete";

    // Templates
    private static final String TEMPLATE_MANAGE_RULE = "admin/plugins/form/modules/comparevalidators/manage_rule.html";

    // JSPs
    private static final String JSP_MANAGE_VALIDATOR = "jsp/admin/plugins/form/ManageValidator.jsp";

    // Plugin
    private static Plugin _plugin;

    // Paginator
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Returns the unique instance of the plugin
     * @return the unique instance of the plugin
     */
    private static Plugin getPlugin(  )
    {
        if ( _plugin == null )
        {
            _plugin = PluginService.getPlugin( CompareValidatorsPlugin.PLUGIN_NAME );
        }

        return _plugin;
    }

    /**
    * Returns the validator interface
    * @param request {@link HttpServletRequest}
    * @param nIdForm the form id
    * @return the validator interface
    */
    public String getUI( HttpServletRequest request, int nIdForm )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_ITEMS_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        List<Rule> listRule = RuleHome.findRulesByForm( nIdForm, getPlugin(  ) );

        Paginator<Rule> paginator = new Paginator<Rule>( listRule, _nItemsPerPage,
                AppPathService.getBaseUrl( request ) + JSP_MANAGE_VALIDATOR, Paginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( _nItemsPerPage ) );
        model.put( MARK_RULE_LIST, paginator.getPageItems(  ) );
        model.put( CompareValidatorsConstants.MARK_ID_FORM, nIdForm );
        model.put( CompareValidatorsConstants.MARK_ENTRY_LIST, CompareValidatorsService.getAuthorizedEntries( nIdForm ) );
        model.put( CompareValidatorsConstants.MARK_OPERATOR_LIST, OperatorHome.findAll( getPlugin(  ) ) );
        addPermissions( request, model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RULE, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Add permissions in the model
     * @param request {@link HttpServletRequest}
     * @param model the model
     */
    private void addPermissions( HttpServletRequest request, Map<String, Object> model )
    {
        // Create
        boolean bPermissionCreate = false;

        if ( RBACService.isAuthorized( Rule.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    CompareValidatorsResourceIdService.PERMISSION_CREATE, AdminUserService.getAdminUser( request ) ) )
        {
            bPermissionCreate = true;
        }

        model.put( MARK_PERMISSION_CREATE, bPermissionCreate );

        // Delete
        boolean bPermissionDelete = false;

        if ( RBACService.isAuthorized( Rule.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    CompareValidatorsResourceIdService.PERMISSION_DELETE, AdminUserService.getAdminUser( request ) ) )
        {
            bPermissionDelete = true;
        }

        model.put( MARK_PERMISSION_DELETE, bPermissionDelete );
    }

    /**
    * Checks if the validator is associated with the form
    * @param nIdForm the form id
    * @return true if the validator is associated with the form, otherwise false
    */
    public boolean isAssociatedWithForm( int nIdForm )
    {
        return ( RuleHome.findRulesByForm( nIdForm, getPlugin(  ) ).size(  ) > 0 );
    }

    /**
     * Removes the associations with the form
     * @param nIdForm the form id
     */
    public void removeAssociationsWithForm( int nIdForm )
    {
        for ( Rule rule : RuleHome.findRulesByForm( nIdForm, getPlugin(  ) ) )
        {
            RuleHome.remove( rule.getIdRule(  ), getPlugin(  ) );
        }
    }

    /**
     * Validates the form
     * @param request {@link HttpServletRequest}
     * @param formSubmit the form submit
     * @param formPlugin the form plugin
     * @throws SiteMessageException throws SiteMessageException
     */
    public void validateForm( HttpServletRequest request, FormSubmit formSubmit, Plugin formPlugin )
        throws SiteMessageException
    {
        List<Rule> listRules = RuleHome.findRulesByForm( formSubmit.getForm(  ).getIdForm(  ), getPlugin(  ) );
        Operator operator;
        IComparator comparator;

        // Saves the response values into the rules
        for ( Response response : formSubmit.getListResponse(  ) )
        {
            for ( Rule rule : listRules )
            {
                if ( response.getEntry(  ).getIdEntry(  ) == rule.getIdEntry1(  ) )
                {
                    rule.setValueEntry1( response.getValueResponse(  ) );
                }
                else if ( response.getEntry(  ).getIdEntry(  ) == rule.getIdEntry2(  ) )
                {
                    rule.setValueEntry2( response.getValueResponse(  ) );
                }
            }
        }

        // Checks that all rules are followed
        for ( Rule rule : listRules )
        {
            // Gets the current operator
            operator = OperatorHome.findByPrimaryKey( rule.getIdOperator(  ), getPlugin(  ) );

            if ( StringUtils.isNotBlank( operator.getClassName(  ) ) )
            {
                try
                {
                    // Instanciates the comparator
                    comparator = (IComparator) Class.forName( operator.getClassName(  ) ).newInstance(  );

                    // Compares the response values
                    if ( !comparator.compare( rule.getValueEntry1(  ), rule.getValueEntry2(  ) ) )
                    {
                        IEntry entry1 = EntryHome.findByPrimaryKey( rule.getIdEntry1(  ), formPlugin );
                        IEntry entry2 = EntryHome.findByPrimaryKey( rule.getIdEntry2(  ), formPlugin );

                        Object[] messageArgs = { entry1.getTitle(  ), entry2.getTitle(  ) };

                        SiteMessageService.setMessage( request, comparator.getMessage(  ), messageArgs,
                            SiteMessage.TYPE_STOP );
                    }
                }
                catch ( InstantiationException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
                catch ( IllegalAccessException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
                catch ( ClassNotFoundException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
            }
        }
    }
}
