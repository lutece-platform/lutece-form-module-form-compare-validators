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
package fr.paris.lutece.plugins.form.modules.comparevalidators.service;

import fr.paris.lutece.plugins.form.business.Form;
import fr.paris.lutece.plugins.form.modules.comparevalidators.util.CompareValidatorsConstants;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.plugins.genericattributes.business.EntryFilter;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;


/**
 * CompareValidatorsService
 */
public final class CompareValidatorsService
{
    // Properties
    private static final String PROPERTY_ENTRY_TYPE_AUTHORIZED = "form-compare-validators.entrytype.authorized";

    // Plugin
    private static final Plugin PLUGIN_FORM = PluginService.getPlugin( FormPlugin.PLUGIN_NAME );

    /**
     * Private constructor
     */
    private CompareValidatorsService( )
    {
    }

    /**
     * Loads data from all authorized entries for the specified form
     * 
     * @param nIdForm the form identifier
     * @return the list which contains entries
     */
    public static Collection<Entry> getAuthorizedEntries( int nIdForm )
    {
        // Gets authorized entry types identifiers
        Collection<Integer> listIdEntryType = getAuthorizedIdEntryTypes( );

        // Filter : form identifier and no conditional question
        EntryFilter entryFilter = new EntryFilter( );
        entryFilter.setFieldDependNull( EntryFilter.FILTER_TRUE );
        entryFilter.setIdResource( nIdForm );
        entryFilter.setResourceType( Form.RESOURCE_TYPE );

        // Filter : authorized entry types
        Collection<Entry> listEntry = new ArrayList<Entry>( );

        for ( Entry entry : EntryHome.getEntryList( entryFilter ) )
        {
            if ( listIdEntryType.contains( entry.getEntryType( ).getIdType( ) ) )
            {
                listEntry.add( entry );
            }
        }

        return listEntry;
    }

    /**
     * Gets authorized entry types identifiers
     * 
     * @return the list which contains all authorized entry types identifiers
     */
    private static Collection<Integer> getAuthorizedIdEntryTypes( )
    {
        Collection<Integer> listIdEntryType = new ArrayList<Integer>( );

        String strListIdEntryType = AppPropertiesService.getProperty( PROPERTY_ENTRY_TYPE_AUTHORIZED );

        if ( StringUtils.isNotBlank( strListIdEntryType ) )
        {
            for ( String strIdEntryType : strListIdEntryType.split( CompareValidatorsConstants.COMMA ) )
            {
                try
                {
                    listIdEntryType.add( Integer.parseInt( strIdEntryType ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe.getLocalizedMessage( ), nfe );
                }
            }
        }

        return listIdEntryType;
    }
}
