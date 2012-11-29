/*
 *    WebLEAFTest, a web application to test and showcase the features of the
 *    WebLEAF framework.
 *    Copyright (C) 2008 Universidad de las Islas Baleares(UIB),
 *                       Cra. Valldemossa, km 7.5
 *                       07071 Palma de Mallorca(Illes Balears)
 *                       Espa�a
 *    This software is the confidential intellectual property of
 *    the UIB; it is copyrighted and licensed, not sold.
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation; either version 2.1 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
Ext.onReady(function()
{
  main();
});

function main()
{
  var tabs = new Ext.TabPanel({
    renderTo: 'Management'
    ,minTabWidth: 115
    ,tabWidth:135
    ,enableTabScroll:true
    ,width:730
    ,height:430
    ,deferredRender: false
    ,activeTab: 1
    ,frame:true
    //,defaults:{autoHeight: false, autoScroll:true}
    ,layoutOnTabChange: true
    ,id:'mainTabPanel'
  });

  var intro = tabs.add({
    title: 'Introduction',
    contentEl: "introTest"
  });

  // We add one tab per implementation
  
  //Static
  var ite_static_mgmt = item_management('Static','static');
  var tab_ite_static_mgmt = tabs.add(ite_static_mgmt);
  tab_ite_static_mgmt.on("activate", function(tab)
  {
    ite_static_mgmt.getStore().reload();
  });

  // Groovy
  var ite_groovy_mgmt = item_management('Groovy','groovy');
  var tab_ite_groovy_mgmt = tabs.add(ite_groovy_mgmt);
  tab_ite_groovy_mgmt.on("activate", function(tab)
  {
    ite_groovy_mgmt.getStore().reload();
  });

  // Compiled Groovy
  var ite_groovyc_mgmt = item_management('Compiled Groovy','groovyc');
  var tab_ite_groovyc_mgmt = tabs.add(ite_groovyc_mgmt);
  tab_ite_groovyc_mgmt.on("activate", function(tab)
  {
    ite_groovyc_mgmt.getStore().reload();
  });

  // PHP
  var ite_php_mgmt = item_management('PHP','php');
  var tab_ite_php_mgmt = tabs.add(ite_php_mgmt);
  tab_ite_php_mgmt.on("activate", function(tab)
  {
    ite_php_mgmt.getStore().reload();
  });

  // JavaScript
  var ite_js_mgmt = item_management('JavaScript','js');
  var tab_ite_js_mgmt = tabs.add(ite_js_mgmt);
  tab_ite_js_mgmt.on("activate", function(tab)
  {
    ite_js_mgmt.getStore().reload();
  });

  // JavaScript E4X
  var ite_jse4x_mgmt = item_management('JavaScript/E4x','js-e4x');
  var tab_ite_jse4x_mgmt = tabs.add(ite_jse4x_mgmt);
  tab_ite_jse4x_mgmt.on("activate", function(tab)
  {
    ite_jse4x_mgmt.getStore().reload();
  });

  // Jython
  var ite_jython_mgmt = item_management('Jython','jython');
  var tab_ite_jython_mgmt = tabs.add(ite_jython_mgmt);
  tab_ite_jython_mgmt.on("activate", function(tab)
  {
    ite_jython_mgmt.getStore().reload();
  });

  // Java Hibernate
  var ite_hb_mgmt = item_management('Hibernate','hb');
  var tab_ite_hb_mgmt = tabs.add(ite_hb_mgmt);
  tab_ite_hb_mgmt.on("activate", function(tab)
  {
    ite_hb_mgmt.getStore().reload();
  });

  // Java JPA
  var ite_jpa_mgmt = item_management('JPA','jpa');
  var tab_ite_jpa_mgmt = tabs.add(ite_jpa_mgmt);
  tab_ite_jpa_mgmt.on("activate", function(tab)
  {
    ite_jpa_mgmt.getStore().reload();
  });

  // Java JDBC
  var ite_jdbc_mgmt = item_management('JDBC','jdbc');
  var tab_ite_jdbc_mgmt = tabs.add(ite_jdbc_mgmt);
  tab_ite_jdbc_mgmt.on("activate", function(tab)
  {
    ite_jdbc_mgmt.getStore().reload();
  });

  // Java Ibatis
  var ite_ib_mgmt = item_management('Ibatis','ib');
  var tab_ite_ib_mgmt = tabs.add(ite_ib_mgmt);
  tab_ite_ib_mgmt.on("activate", function(tab)
  {
    ite_ib_mgmt.getStore().reload();
  });

  // Initialize the tab panel and show the intro message
  tabs.render();
  intro.show();
    // Hide the "loading" layer
  setTimeout(function()
  {
    Ext.get('loading-mask').fadeOut({remove:true});
  }, 250);

}
