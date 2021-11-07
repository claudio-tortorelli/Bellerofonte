package claudiosoft.bellerofonte;

import java.io.*;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.*; // to processing regular expression

/**
 * 
 * In this class on define what web pages
 * are tested in the execute of Macro_test
 * and related options  
 */
class TestOption implements Serializable
{
/** 
 * Fields
 */
 	private String _protocol = "";
 	private String _host = "";
 	private String _path = "";
 	private String _fileName = "";
 	private boolean _cookie;
 	private boolean _redirect;
 	private boolean _refresh;
 	private boolean _iFrame;
 	private String _userAgent = "";
 	private boolean _defOpt;
 	private boolean _msgLen;
 	private String _defChar;
	private String _defType;
	private boolean _errExc;
	private boolean _errScript;
	private boolean _imgTxt;
	private boolean _httpHead;
	private boolean _caseSens;
	private boolean _paramValidate;
	private boolean _parseWarn;
	private boolean _postChar;
	private String _redirDelay = "";
	private boolean _scripting;			
	
/**
 ***********************************************
 * Constructor
 */
	public TestOption() 
	{		
	}	
	
/**
 ***********************************************
 * Getter method
 */
	public String getProtocol()
	{
		return _protocol;
	}
	
/**
 ***********************************************
 * Getter method
 */
	public String getHost()
	{
		return _host;
	}

/**
 ***********************************************
 * Getter method
 */
	public String getPath()
	{
		return _path;
	}

/**
 ***********************************************
 * Getter method
 */
	public String getFileName()
	{
		return _fileName;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getCookie()
	{
		return _cookie;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getAutoRedirect()
	{
		return _redirect;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getAutoRefresh()
	{
		return _refresh;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getFloatFrame()
	{
		return _iFrame;
	}

/**
 ***********************************************
 * Getter method
 */
	public String getUserAgent()
	{
		return _userAgent;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getDefOptions()
	{
		return _defOpt;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getMsgLength()
	{
		return _msgLen;
	}

/**
 ***********************************************
 * Getter method
 */
	public String getDefCharset()
	{
		return _defChar;
	}
 	
/**
 ***********************************************
 * Getter method
 */
	public String getDefType()
	{
		return _defType;
	}
	
	
/**
 ***********************************************
 * Getter method
 */
	public boolean getExceptionOnError()
	{
		return _errExc;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getExceptionOnScript()
	{
		return _errScript;
	}
		
/**
 ***********************************************
 * Getter method
 */
	public boolean getImageAsTxt()
	{
		return _imgTxt;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getHttpHeaderLogging()
	{
		return _httpHead;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getMatchingSensitive()
	{
		return _caseSens;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getValidateParameters()
	{
		return _paramValidate;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getParsingWarning()
	{
		return _parseWarn;
	}
		
/**
 ***********************************************
 * Getter method
 */
	public boolean getPostCharset()
	{
		return _postChar;
	}

/**
 ***********************************************
 * Getter method
 */
	public String getRedirectDelay()
	{
		return _redirDelay;
	}

/**
 ***********************************************
 * Getter method
 */
	public boolean getScriptingEnabled()
	{
		return _scripting;
	}
	

/**
 ***********************************************
 * Setter method: this get the Properties readed from
 * file and set the relative options.
 */
	public void setUp(Properties opt) throws Exception
	{	
	// complete for a valid url
		_protocol = opt.getProperty("protocol").trim();
		if (!_protocol.endsWith("://"))
		{
			_protocol = _protocol + "://";
		}
		_host = opt.getProperty("host").trim();
		if (!_host.endsWith("/"))
		{
			_host = _host + "/";
		}
		if (_host.startsWith("/"))
		{
			_host = _host.substring(1,_host.length());
		}		
		_path = opt.getProperty("path").trim();
		if (!_path.endsWith("/") && !_path.equals(""))
		{
			_path = _path + "/";
		}		
		if (_path.startsWith("/") && !_path.equals(""))
		{
			_path = _path.substring(1,_path.length());
		}				
		_fileName = opt.getProperty("fileBase").trim();		
		if (_fileName.startsWith("/"))
		{
			_fileName = _fileName.substring(1,_fileName.length());
		}
	// set other option
	//cookie
		String tmp = opt.getProperty("cookie").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _cookie = true; }
		else
		{ _cookie = false; }
	//autoredirect
		tmp = opt.getProperty("autoRedirect").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _redirect = true; }
		else
		{ _redirect = false; }
	//autorefresh
		tmp = opt.getProperty("autoRefresh").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _refresh = true; }
		else
		{ _refresh = false; }
	//support float frame
		tmp = opt.getProperty("floatFrame").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _iFrame = true; }
		else
		{ _iFrame = false; }
	//user agent's name
		_userAgent = opt.getProperty("userAgent");			
	//default http unit option
		tmp = opt.getProperty("useDefaultOptions").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _defOpt = true; }
		else
		{ _defOpt = false; }
	//message length verify
		tmp = opt.getProperty("checkHttpMsgLength").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _msgLen = true; }
		else
		{ _msgLen = false; }
	//default character set
		_defChar = opt.getProperty("defaultCharacterSet").toUpperCase(); 		
	//default content type
		_defType = opt.getProperty("defaultContentType").toUpperCase(); 
	//generate exceptions on errors
		tmp = opt.getProperty("exceptionThrownOnErrorStatus").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _errExc = true; }
		else
		{ _errExc = false; }
	//generate exceptions on script errors
		tmp = opt.getProperty("exceptionThrownOnScriptError").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _errScript = true; }
		else
		{ _errScript = false; }	
	//treat images as text
		tmp = opt.getProperty("imageTreatedAsAlternativeTxt").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _imgTxt = true; }
		else
		{ _imgTxt = false; }	
	//logging http headers
		tmp = opt.getProperty("loggingHttpHeaders").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _httpHead = true; }
		else
		{ _httpHead = false; }	
	//ignore case sensitive in matching
		tmp = opt.getProperty("matchingIgnoreCase").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _caseSens = true; }
		else
		{ _caseSens = false; }	
	//check for the true parameter's value
		tmp = opt.getProperty("parametersValueValidate").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _paramValidate = true; }
		else
		{ _paramValidate = false; }	
	//parse warnings
		tmp = opt.getProperty("parserWarnings").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _parseWarn= true; }
		else
		{ _parseWarn = false; }	
	//if the post includes charset
		tmp = opt.getProperty("postIncludesCharset").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _postChar = true; }
		else
		{ _postChar  = false; }	
	//redirect delay
		_redirDelay = opt.getProperty("redirectDelay").toUpperCase(); 
	//enable scripting
		tmp = opt.getProperty("scripting").toUpperCase(); 
		if (tmp.equals("YES"))
		{ _scripting = true; }
		else
		{ _scripting = false; }			
	}

/**
 ***********************************************
 * Copy constructor
 */
	public TestOption(TestOption other) 
	{
		if(this != other) 
		{
			this._protocol = other._protocol;
			this._host = other._host;
			this._path = other._path;
			this._fileName = other._fileName;
			this._cookie = other._cookie;
			this._redirect = other._redirect;
			this._refresh = other._refresh;
			this._iFrame = other._iFrame;
			this._userAgent = other._userAgent;
			this._defOpt = other._defOpt;
			this._msgLen = other._msgLen;
			this._defChar = other._defChar;
			this._defType = other._defType;
			this._errExc = other._errExc;
			this._errScript = other._errScript;
			this._imgTxt = other._imgTxt;
			this._httpHead = other._httpHead;
			this._caseSens = other._caseSens;
			this._paramValidate = other._paramValidate;
			this._parseWarn = other._parseWarn;
			this._postChar = other._postChar;
			this._redirDelay = other._redirDelay;
			this._scripting = other._scripting;
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
		buffer.append("_protocol = ");
		buffer.append(_protocol);
		buffer.append(sep);
		buffer.append("_host = ");
		buffer.append(_host);
		buffer.append(sep);
		buffer.append("_path = ");
		buffer.append(_path);
		buffer.append(sep);
		buffer.append("_fileName = ");
		buffer.append(_fileName);
		buffer.append(sep);
		buffer.append("_cookie = ");
		buffer.append(_cookie);
		buffer.append(sep);
		buffer.append("_redirect = ");
		buffer.append(_redirect);
		buffer.append(sep);
		buffer.append("_refresh = ");
		buffer.append(_refresh);
		buffer.append(sep);
		buffer.append("_iFrame = ");
		buffer.append(_iFrame);
		buffer.append(sep);
		buffer.append("_userAgent = ");
		buffer.append(_userAgent);
		buffer.append(sep);
		buffer.append("_defOpt = ");
		buffer.append(_defOpt);
		buffer.append(sep);
		buffer.append("_msgLen = ");
		buffer.append(_msgLen);
		buffer.append(sep);
		buffer.append("_defChar = ");
		buffer.append(_defChar);
		buffer.append(sep);
		buffer.append("_defType = ");
		buffer.append(_defType);
		buffer.append(sep);
		buffer.append("_errExc = ");
		buffer.append(_errExc);
		buffer.append(sep);
		buffer.append("_errScript = ");
		buffer.append(_errScript);
		buffer.append(sep);
		buffer.append("_imgTxt = ");
		buffer.append(_imgTxt);
		buffer.append(sep);
		buffer.append("_httpHead = ");
		buffer.append(_httpHead);
		buffer.append(sep);
		buffer.append("_caseSens = ");
		buffer.append(_caseSens);
		buffer.append(sep);
		buffer.append("_paramValidate = ");
		buffer.append(_paramValidate);
		buffer.append(sep);
		buffer.append("_parseWarn = ");
		buffer.append(_parseWarn);
		buffer.append(sep);
		buffer.append("_postChar = ");
		buffer.append(_postChar);
		buffer.append(sep);
		buffer.append("_redirDelay = ");
		buffer.append(_redirDelay);
		buffer.append(sep);
		buffer.append("_scripting = ");
		buffer.append(_scripting);
		buffer.append(sep);
		
		return buffer.toString();
	}	
}// end of class
