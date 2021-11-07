package claudiosoft.btreestring;

/**
 * This class represent the btree with nodes and
 * operations on nodes
 */

class NodeString 
{
/**
 * Fields
 */
 	private String _value = "";
 	private int _position = 0;
 	private NodeString _leftChild = null;
 	private NodeString _rightChild = null;
 	private boolean _marked = false;
 
/**
 * Constructor
 */
	public NodeString(String val, int pos)
	{
		_value = val;
		_position = pos;
	}

/**
 * This method return the value of node
 */	
 	public String getValue()
 	{
 		return _value;
 	} 	

/**
 * This method set node value
 */	
 	public void setValue(String key)
 	{
 		_value = key;
 	} 	

/**
 * This method return the position of node
 */	
 	public int getPosition()
 	{
 		return _position;
 	} 	

/**
 * This method set node position
 */	
 	public void setPosition(int pos)
 	{
 		_position = pos;
 	} 	
 
/**
 * This method get left child
 */	
 	public NodeString left()
 	{
 		return _leftChild;
 	} 	

/**
 * This method get right child
 */	
 	public NodeString right()
 	{
 		return _rightChild;
 	} 	
 	
/**
 * This method set left child
 */	
 	public void setLeft(String key, int pos)
 	{
 		if (_leftChild != null)
 		{
 			_leftChild.setValue(key);
 			_leftChild.setPosition(pos);
 		}
 		else
 		{
 			_leftChild = new NodeString(key, pos);
 		}
 	} 	

/**
 * This method set right child
 */	
 	public void setRight(String key, int pos)
 	{
 		if (_rightChild != null)
 		{
 			_rightChild.setValue(key);
 			_rightChild.setPosition(pos);
 		}
 		else
 		{
 			_rightChild = new NodeString(key,pos);
 		}
 	} 
 	
/**
 * check if the node is a leaf	
 */	
 	public boolean isLeaf()
 	{
 		boolean leaf = false;
 		if (_rightChild == null && _leftChild == null)
 		{
 			leaf = true;
 		}
 		return leaf;
 	} 
 
/**
 * This method del left child
 */	
 	public void delLeft()
 	{
 		_leftChild = null;
 	} 	

/**
 * This method del right child
 */	
 	public void delRight()
 	{
 		_rightChild = null;
 	} 	

/**
 * This method mark the node as visited
 */	
 	public void mark()
 	{
 		_marked = true;
 	} 	

/**
 * This method unmark the node 
 */	
 	public void unMark()
 	{
 		_marked = false;
 	} 	

/**
 * This method return if the node is marked
 */	
 	public boolean isMark()
 	{
 		return _marked;
 	} 	
}