/** 
 * SIMPLE BTREE MANAGER
 * This auxiliary package create and manage with
 * simple operations binary tree structures. It
 * not display anything and permit only some basic
 * modalities of insertion and recover information
 * on btrees.
 *
 *	ACTUALLY ONLY FOR STRING
 *
 * @author Claudio Tortorelli
 * Started 05/07/2003	
 */

package claudiosoft.btreestring;

/**
 * This class represent the btree with nodes and
 * operations on nodes
 */

public class BtreeString 
{
/**
 * Fields
 */
 	private NodeString _root = null; 
 	private BtreeString _subTree = null;
	 
/**
 * Constructor
 */
 	public BtreeString()
 	{ 		
 	}
 
/**
 * setRoot
 */ 
	public void setRoot(NodeString node)
	{
		_root = node;
	}

/**
 * This method insert a key string with his position
 */
	public void insert(String key, int pos)
	{
	// key is root
		if (_root == null)
		{
		 	this.setRoot(new NodeString(key, pos));
		}
	// check if there are child of root
		else
		{
		// go to left child
			if (_root.getValue().compareTo(key) > 0)			
			{
				if (_root.left() == null)
				{
					_root.setLeft(key, pos);
				}
			// repeat the insert on left subtree
				else
				{				
					_subTree = new BtreeString();
					_subTree.setRoot(_root.left());
					_subTree.insert(key, pos);					
				}
			}
		// go to the right child
			if (_root.getValue().compareTo(key) < 0)			
			{			
				if (_root.right() == null)
				{
					_root.setRight(key, pos);
				}
			// repeat the insert on right subtree
				else
				{				
					_subTree = new BtreeString();
					_subTree.setRoot(_root.right());
					_subTree.insert(key, pos);					
				}
			}
		}
	}
 	
/**
 * This method find a key and return its's vector position
 */
 	public int find(String key)
 	{
 		int pos = -1;
 	// root is empty
		if (_root == null)
		{
		 	pos = -1;		 	
		}
	// check the root
		else
		{
		// ok: it's the root: found!
			if (_root.getValue().compareTo(key) == 0)			
			{			
				pos = _root.getPosition();			
			}
		// go to left child
			else if (_root.getValue().compareTo(key) > 0)			
			{
				_subTree = new BtreeString();
				_subTree.setRoot(_root.left());
				pos = _subTree.find(key);
			}
		// go to the right child
			else if (_root.getValue().compareTo(key) < 0)			
			{	
				_subTree = new BtreeString();
				_subTree.setRoot(_root.right());
				pos = _subTree.find(key);				
			}		
		} 						
		return pos;
 	}

/**
 * This method find and mark a key
 */
/* 	public boolean findAndMark(String key)
 	{
 		boolean found = false;
 	// root is empty
		if (_root == null)
		{
		 	found = false;
		}
	// check the root
		else
		{
		// go to left child
			if (_root.getValue().compareTo(key) > 0)			
			{
				_subTree = new BtreeString();
				_subTree.setRoot(_root.left());
				if (_subTree.find(key))
				{
					found = true;
				}
				else
				{
					found = false;
				}
			}
		// go to the right child
			if (_root.getValue().compareTo(key) < 0)			
			{	
				_subTree = new BtreeString();
				_subTree.setRoot(_root.right());
				if (_subTree.find(key))
				{
					found = true;
				}
				else
				{
					found = false;
				}		
			}
		// found!
			if (_root.getValue().compareTo(key) == 0)			
			{			
				_root.mark();
				found = true;
				System.out.println ("sono qui");
			}
		} 				
		return found;
 	}
 */

/**
 * This method check if all nodes are marked
 */

/*
 	public boolean allMarked()
 	{
 		boolean yes = false;
 		if (_root != null)
 		{
 		// root is marked
 			if (_root.isMark())
 			{	
 			System.out.println ("is mark");
 			// go to subtrees
 				yes = true; 			
 			// check sub tree left
 				if (_root.left() != null)
 				{
 					_subTree = new BtreeString();
 					_subTree.setRoot(_root.left());
 					yes = _subTree.allMarked();
 				}
 			// subtree left are marked check subtree right
 				if (yes)
 				{
 					if (_root.right() != null)
 					{
 						_subTree = new BtreeString();
 						_subTree.setRoot(_root.right());
 						yes = _subTree.allMarked();
 					}	
 				}
 			}
 		// root isn't marked
 			else
 			{
 				yes = false;
 			}
 		}	
 	// root is null and yes = true
 		else
 		{
 			yes = true;
 		}
 		return yes;
 	}
 */	
/**
 * This method display all nodes of tree in anticipated order
 */
 	public void display()
 	{
 		if (_root != null)
 		{
 		// display root and childs
 			System.out.print ("root: " + _root.getValue() + " " + _root.getPosition());
 			if (_root.left() != null) {
 				System.out.print (" - left child: " + _root.left().getValue());
 			}
 			else {
 				System.out.print (" - left child: null");
 			}
 			if (_root.right() != null) {
 				System.out.println (" - right child: " + _root.right().getValue());
 			}
 			else {
 				System.out.println(" - right child: null");
 			} 			
 		// reapply to subtrees 
 			if (_root.left() != null)
 			{ 		
 				_subTree = new BtreeString();		 				
 				_subTree.setRoot(_root.left());
 				_subTree.display();
 			}
 			if (_root.right() != null)
 			{
 				_subTree = new BtreeString();
 				_subTree.setRoot(_root.right());
 				_subTree.display();
 			}
 		}	
 	// root is null and yes = true
 		else
 		{
 			System.out.println ("root: null");
 		} 
 	}
}
