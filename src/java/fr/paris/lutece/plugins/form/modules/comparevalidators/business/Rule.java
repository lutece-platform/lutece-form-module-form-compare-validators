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

/**
 * Rule
 */
public class Rule
{
    public static final String RESOURCE_TYPE = "FORM_RULE";
    private int _nIdRule;
    private int _nIdForm;
    private int _nIdEntry1;
    private String _strValueEntry1;
    private int _nIdEntry2;
    private String _strValueEntry2;
    private int _nIdOperator;

    /**
     * Gets the rule identifier
     * @return the rule identifier
     */
    public int getIdRule( )
    {
        return _nIdRule;
    }

    /**
     * Sets the rule identifier
     * @param nIdRule the rule identifier
     */
    public void setIdRule( int nIdRule )
    {
        _nIdRule = nIdRule;
    }

    /**
     * Gets the form identifier
     * @return the form identifier
     */
    public int getIdForm( )
    {
        return _nIdForm;
    }

    /**
     * Sets the form identifier
     * @param nIdForm the form identifier
     */
    public void setIdForm( int nIdForm )
    {
        _nIdForm = nIdForm;
    }

    /**
     * Gets the first entry identifier
     * @return the first entry identifier
     */
    public int getIdEntry1( )
    {
        return _nIdEntry1;
    }

    /**
     * Sets the first entry identifier
     * @param nIdEntry1 the first entry identifier
     */
    public void setIdEntry1( int nIdEntry1 )
    {
        _nIdEntry1 = nIdEntry1;
    }

    /**
     * Gets the response value for the first entry
     * @return the response value for the first entry
     */
    public String getValueEntry1( )
    {
        return _strValueEntry1;
    }

    /**
     * Sets the response value for the first entry
     * @param strValueEntry1 the response value for the first entry
     */
    public void setValueEntry1( String strValueEntry1 )
    {
        _strValueEntry1 = strValueEntry1;
    }

    /**
     * Gets the second entry identifier
     * @return the second entry identifier
     */
    public int getIdEntry2( )
    {
        return _nIdEntry2;
    }

    /**
     * Sets the second entry identifier
     * @param nIdEntry2 the second entry identifier
     */
    public void setIdEntry2( int nIdEntry2 )
    {
        _nIdEntry2 = nIdEntry2;
    }

    /**
     * Gets the response value for the second entry
     * @return the response value for the second entry
     */
    public String getValueEntry2( )
    {
        return _strValueEntry2;
    }

    /**
     * Sets the response value for the second entry
     * @param strValueEntry2 the response value for the second entry
     */
    public void setValueEntry2( String strValueEntry2 )
    {
        _strValueEntry2 = strValueEntry2;
    }

    /**
     * Gets the operator identifier
     * @return the operator identifier
     */
    public int getIdOperator( )
    {
        return _nIdOperator;
    }

    /**
     * Sets the operator identifier
     * @param nIdOperator the operator identifier
     */
    public void setIdOperator( int nIdOperator )
    {
        _nIdOperator = nIdOperator;
    }
}
