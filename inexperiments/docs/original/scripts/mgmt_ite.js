/*
 *    WebLEAFTest, a web application to test and showcase the features of the
 *    WebLEAF framework.
 *    Copyright (C) 2008 Universidad de las Islas Baleares(UIB),
 *                       Cra. Valldemossa, km 7.5
 *                       07071 Palma de Mallorca(Illes Balears)
 *                       España
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
function item_management(title, preffix)
{
    var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: preffix + '/showItemsJSON.test'
        })
        ,reader: new Ext.data.JsonReader({
            root: 'items',
            totalProperty: 'total',
            id: 'Code'
        },[
            {name: 'Code', mapping: 'Code'},
            {name: 'Name', mapping: 'Name'},
            {name: 'Description', mapping: 'Description'}
        ])
    });

    // the DefaultColumnModel expects this blob to define columns. It can be extended to provide
    // custom or reusable ColumnModels
    var colModel = new Ext.grid.ColumnModel([{
      id:'Name'
      ,align: "left"
      ,header: "Name"
      ,sortable: true
      ,dataIndex: 'Name'
      ,editor: new Ext.form.TextField({ allowBlank: false })
      },
      {
      id:'Description'
      ,width: 350
      ,align: "left"
      ,header: "Description"
      ,sortable: false
      ,dataIndex: 'Description'
      ,editor: new Ext.form.TextField({ allowBlank: true })
      }
      ]);

    var theSelectionModel = new Ext.grid.RowSelectionModel({singleSelect:true});
    theSelectionModel.on('selectionchange',selectedRow);
    theSelectionModel.onEditorKey = function(field, e)
    {
      var k = e.getKey(), newCell, g = theSelectionModel.grid, ed = g.activeEditor;
      if(k == e.ENTER && !e.ctrlKey){
            e.stopEvent();
            ed.completeEdit();
      }
      else if(k == e.ESC){
            ed.cancelEdit();
        }
    };

     // create the Grid
     var grid = new Ext.grid.EditorGridPanel({
         ds: ds
         ,cm: colModel
         ,sm: theSelectionModel
         ,autoExpandColumn: 'Name'
         ,sortInfo:{field: 'Name', direction: "DESC"}
         ,height:450
         ,width:730
         ,enableHdMenu: false
         ,title:title
         ,frame:true
         ,clicksToEdit:2
         // inline toolbars
         ,tbar:[{
             text:'Add item'
             ,handler: onItemAdd
             ,iconCls:'add'
          },'-',{
             text:'Remove item'
             ,handler: onItemRem
             ,iconCls:'remove'
             ,disabled : true
             ,id:'remItemTbarButton'
          }]
     });
    grid.on('afteredit',onItemMod);

    grid.reload = function()
    {
      this.getStore().reload();
    }

   var itemWindow;

   function selectedRow()
   {
    var selected = grid.getSelectionModel().getSelected();
		if(selected)
		{
		  grid.getTopToolbar().items.map.remItemTbarButton.enable();
		}
		else
		{
	      grid.getTopToolbar().items.map.remItemTbarButton.disable();
		}
   }

   //
   function onItemMod(event)
   {
    curRow = event.record;
    itemMod( event.record.data.Code
              ,event.record.data.Name
              ,event.record.data.Description
              ,curRow);
   }

   // functions to display feedback
   function onItemAdd(btn)
   {
       if(!itemWindow)
       {
	    	var theFormPanel = new Ext.FormPanel({
	        labelWidth: 75, // label settings here cascade unless overridden
	        url: preffix + '/addItemJSON.test',
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        width: 350,
	        defaults: {width: 230},
	        defaultType: 'textfield',
	        waitMsgTarget: true,
	        items: [{
	                fieldLabel: 'Name'
	                ,name: 'w_ite_name'
	                ,allowBlank:false
	                ,anchor:'100%'
	            }
	            , {
	              fieldLabel: 'Description'
	              ,name: 'w_ite_description'
                ,allowBlank:true
                ,anchor:'100%'
	            }
	            ]
   			});

	    	itemWindow = new Ext.Window({
	        title: 'Adding new item',
	        width: 500,
	        height:200,
	        layout: 'fit',
	        modal: true,
	        plain:true,
	        bodyStyle:'padding:5px;',
	        buttonAlign:'center',
	        items: theFormPanel,
	        buttons: [{
	            text: 'Add'
	            , handler: function()
	              {
	               if( theFormPanel.form.isValid())
	               {
	                 theFormPanel.form.submit({
	                   waitMsg:'Adding item...'
	                   ,success: function ( form, action )
  	                  {
                        grid.getStore().reload();
    	                  itemWindow.hide();
    	                  form.reset();
  	                  }
	                   ,failure: function ( form, action)
              				{
              				  if(action.result && action.result.errorInfo)
              				  {
                					Ext.MessageBox.show({
                						title: 'Error!'
                						, minwidth:300
                						, msg: 'Item could not be addded: ' + action.result.errorInfo
                						, icon: Ext.MessageBox.ERROR
                						, buttons: Ext.MessageBox.OK
                						});
              				  }
              				  else
              				  {
              				    Ext.MessageBox.alert('Error', 'Error accessing server!');
              				  }
              				}
	                   });
	               }
	               else
	               {
	                 Ext.MessageBox.alert('Error', 'Please, fill in all the mandatory fields.');
	               }
	              }
	            },
	            {
	            text: 'Cancel'
	            ,handler: function(){itemWindow.hide();}
	        		}]
	    	});
		  }
    itemWindow.show();
   }


    function onItemRem(btn)
    {
     var selected = grid.getSelectionModel().getSelected();
     if(selected)
     {
	     Ext.MessageBox.confirm(
	     'Confirm'
	     , 'Are your sure that you want to remove the item ' + selected.data.Name + '?'
	     , removeGrup
	     );
     }
    }

    function removeGrup(btn)
    {
    	if(btn=='yes')
    	{
	        Ext.MessageBox.show({
	           msg: 'Removing item, please wait...',
	           progressText: 'Removing...',
		       	 modal: true,
	           width:300,
	           height:46,
	           wait:true,
	           waitConfig: {interval:200},
	           icon:'ext-mb-download',
	           animEl: 'mb7'
	       	});

			var selected = grid.getSelectionModel().getSelected();
			Ext.Ajax.request({
				url : preffix + '/remItemJSON.test' ,
				params : { w_ite_code : selected.data.Code },
				method: 'POST',
				success: function ( result, request )
				{
					Ext.MessageBox.hide();
					var jsonData = Ext.util.JSON.decode(result.responseText);
					if(jsonData.result=='success')
					{
						grid.getStore().remove(grid.getSelectionModel().getSelected());
					}
					else
					{
						Ext.MessageBox.show({
							title: 'Error!'
							, minwidth:300
							, msg: 'Item could not be removed: ' + jsonData.message
							, icon: Ext.MessageBox.ERROR
							, buttons: Ext.MessageBox.OK
							});
					}
				},
				failure: function ( result, request)
				{
					Ext.MessageBox.hide();
					Ext.MessageBox.alert('Error', 'Error accessing server!');
				}
			});
   	}
  }
  /**************************************************************************
   * AUXILIARY FUNCTIONS
  **************************************************************************/
   // Actualizacion de un item
   function itemMod(code, nom, admin,curRow)
   {
    // alert('Un item ha sido modificado!!' + event.record.data.Code);
    Ext.MessageBox.show({
        msg: 'Modifying item, please wait...',
        progressText: 'Modifying...',
     	  modal: true,
        width:300,
        height:46,
        wait:true,
        waitConfig: {interval:200},
        icon:'ext-mb-download',
        animEl: 'mb7'
    });

  	Ext.Ajax.request({
  		url : preffix + '/modItemJSON.test' ,
  		params : {
  			w_ite_code : code
  			,w_ite_name : nom
  			,w_ite_description : admin
  			},
  		method: 'POST',
  		success: function ( result, request )
  		{
  			Ext.MessageBox.hide();
  			var jsonData = Ext.util.JSON.decode(result.responseText);
  			if(jsonData.result=='success')
  			{
  			  curRow.commit();
  			}
  			else
  			{
  				Ext.MessageBox.show({
  					title: 'Error!'
  					, minwidth:300
  					, msg: 'Item could not be modified: ' + jsonData.message
  					, icon: Ext.MessageBox.ERROR
  					, buttons: Ext.MessageBox.OK
  					});
  				curRow.reject();
  			}
  		},
  		failure: function ( result, request)
  		{
  			Ext.MessageBox.hide();
  			Ext.MessageBox.alert('Error', 'Error accessing server!');
  			curRow.reject();
  		}
    });
  }
  return grid;
}
