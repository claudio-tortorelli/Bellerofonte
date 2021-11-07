package claudiosoft.bellerofonte;

import java.util.Vector;

/**
 * This class represents the actual web page 
 * readed. It holds some web elements in httpUnit format
 * for default but can keep other type of elements too.
 */ 
class Web_page 
{	
/** 
 * Fields
 */ 	
  	Vector _pageConnection = new Vector();
  	String _target = "";
  		
/**
 ***********************************************
 * Constructor
 */
	public Web_page() 
	{		
	}

/**
 ***********************************************
 * IN: void
 * OUT: vector with actual pageConnection: the element that hold the state of the page
 */
 	public Vector getCurrent()
	{		
		return (Vector)_pageConnection.clone();
	}	
		
/**
 ***********************************************
 * IN: new elements with page state
 * OUT: void
 */
 	public void setCurrent(Vector pageModified)
	{		
		_pageConnection = null;
		_pageConnection = (Vector)pageModified.clone();
	}

/**
 ***********************************************
 * IN: void
 * OUT: actual target
 */
 	public String getTarget()
	{		
		return _target;
	}	
		
/**
 ***********************************************
 * IN: new target
 * OUT: void
 */
 	public void setTarget(String target)
	{		
		_target = target;
	}

/**
 ***********************************************
 * Copy constructor
 */
	public Web_page(Web_page other) 
	{
		if(this != other) 
		{
			this._pageConnection = other._pageConnection;
			this._target = other._target;
		}
	}
	
/**
 ***********************************************
 * toString method
 */
	public String toString() 
	{
		String sep = System.getProperty("line.separator");

		StringBuffer buffer = new StringBuffer();
		buffer.append(sep);
		buffer.append("_pageConnection = ");
		buffer.append(_pageConnection);
		buffer.append(sep);
		buffer.append("_target = ");
		buffer.append(_target);
		buffer.append(sep);
		
		return buffer.toString();
	}
}// end of class
