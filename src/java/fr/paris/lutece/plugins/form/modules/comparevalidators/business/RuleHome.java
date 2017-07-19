/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.form.modules.comparevalidators.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * RuleHome
 */
public final class RuleHome
{
    // Static variable pointed at the DAO instance
    private static IRuleDAO _dao = SpringContextService.getBean( "form-compare-validators.ruleDAO" );

    /**
     * Private constructor - this class does not need to be instantiated
     */
    private RuleHome( )
    {
    }

    /**
     * Creates a new instance of rule
     * 
     * @param rule The instance of rule which contains the informations to store
     * @param plugin The plugin
     * @return The instance of rule which has been created with its primary key
     */
    public static Rule create( Rule rule, Plugin plugin )
    {
        _dao.insert( rule, plugin );

        return rule;
    }

    /**
     * Removes the rule whose identifier is specified in parameter
     * 
     * @param nKey The primary key of the rule to remove
     * @param plugin The plugin
     */
    public static void remove( int nKey, Plugin plugin )
    {
        _dao.delete( nKey, plugin );
    }

    /**
     * Finds all rules for a given form identifier
     * 
     * @param nIdForm the form identifier
     * @param plugin The plugin
     * @return the referenceList which contains the found rules
     */
    public static List<Rule> findRulesByForm( int nIdForm, Plugin plugin )
    {
        return _dao.selectRulesByForm( nIdForm, plugin );
    }
}
