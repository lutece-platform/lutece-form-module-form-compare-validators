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
package fr.paris.lutece.plugins.form.modules.comparevalidators.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * RuleDAO
 */
public class RuleDAO implements IRuleDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max(id_rule) FROM form_compare_validators_rule";
    private static final String SQL_QUERY_INSERT = "INSERT INTO form_compare_validators_rule(id_rule,id_form,id_entry_1,id_entry_2,id_operator) VALUES(?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM form_compare_validators_rule WHERE id_rule=?";
    private static final String SQL_QUERY_SELECT_BY_FORM = "SELECT id_rule,id_entry_1,id_entry_2,id_operator FROM form_compare_validators_rule WHERE id_form=?";

    /**
    * Generates a new primary key
    *
    * @param plugin The plugin
    * @return The new primary key
    */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // If the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Creates a new instance of rule
     *
     * @param rule The instance of rule which contains the informations to store
     * @param plugin The plugin
     */
    public void insert( Rule rule, Plugin plugin )
    {
        rule.setIdRule( newPrimaryKey( plugin ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, rule.getIdRule(  ) );
        daoUtil.setInt( 2, rule.getIdForm(  ) );
        daoUtil.setInt( 3, rule.getIdEntry1(  ) );
        daoUtil.setInt( 4, rule.getIdEntry2(  ) );
        daoUtil.setInt( 5, rule.getIdOperator(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Removes the rule whose identifier is specified in parameter
     *
     * @param nKey The primary key of the rule to remove
     * @param plugin The plugin
     */
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Finds all rules for a given form identifier
     *
     * @param nIdForm the form identifier
     * @param plugin The plugin
     * @return the referenceList which contains the found rules
     */
    public List<Rule> selectRulesByForm( int nIdForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_FORM, plugin );
        daoUtil.setInt( 1, nIdForm );
        daoUtil.executeQuery(  );

        List<Rule> listRules = new ArrayList<Rule>(  );
        Rule rule;

        while ( daoUtil.next(  ) )
        {
            rule = new Rule(  );
            rule.setIdRule( daoUtil.getInt( 1 ) );
            rule.setIdForm( nIdForm );
            rule.setIdEntry1( daoUtil.getInt( 2 ) );
            rule.setIdEntry2( daoUtil.getInt( 3 ) );
            rule.setIdOperator( daoUtil.getInt( 4 ) );

            listRules.add( rule );
        }

        daoUtil.free(  );

        return listRules;
    }
}
