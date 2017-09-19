package com.sap.voting.constants;

/**
 * Created by Arun on 9/16/17.
 */

/**
 * Contains all the SQL queries. This is used for easier maintainability.
 * Any changes to the query happens only at this place.
 */
public class TableNameConstants {
    public static final String SQL_ADD_USER="INSERT INTO PERSON (NAME,EMAIL,PASSWORD) VALUES(?,?,?);";
    public static final String SQL_LOG_IN_USER="UPDATE PERSON SET SESSIONTOKEN=? WHERE EMAIL=? AND PASSWORD=? ;";
    public static final String SQL_LOG_OUT_USER="UPDATE PERSON SET SESSIONTOKEN=?  WHERE SESSIONTOKEN=?" ;
    public static final String SQL_CHECK_USER="SELECT COUNT(*) FROM PERSON WHERE EMAIL=? AND PASSWORD=? ;";
    public static final String SQL_CREATE_POST="INSERT INTO POST (OWNER,NAME,DESCRIPTION,ENDDATE) values (?,?,?,?) ;";
    public static final String SQL_GET_POST_ID="SELECT ID FROM POST WHERE OWNER=? AND NAME=? ;";
    public static final String SQL_CREATE_POST_OPTIONS="INSERT INTO POSTOPTION (POSTID,NAME,DESCRIPTION) values(?,?,?) ;";
    public static final String SQL_MODIFY_POST="UPDATE POST SET ENDDATE=? WHERE OWNER=? AND ID=? ;";
    public static final String SQL_GET_ALL_POST="SELECT ID, NAME, DESCRIPTION, ENDDATE FROM POST WHERE OWNER= ? ;";
    public static final String SQL_GET_POST=" SELECT NAME, DESCRIPTION, ENDDATE FROM POST WHERE OWNER = ? AND ID= ?;";
    public static final String SQL_GET_POSTOPTIONS="SELECT ID, NAME, DESCRIPTION FROM POSTOPTION WHERE POSTID= ? :";
    public static final String SQL_GET_USER_ID="SELECT ID FROM PERSON WHERE SESSIONTOKEN=? ;";
    public static final String SQL_CAST_VOTE="INSERT INTO VOTE (VID,PID,OID) VALUES (?,?,?);";
    public static final String SQL_GET_VOTE="SELECT COUNT FROM VOTE WHERE VID=? AND PID = ? ;";
    public static final String SQL_MODIFY_VOTE="UPDATE VOTE SET OID= ? WHERE VID =? AND PID=? WHERE enddate > NOW() ;";
    public static final String SQL_CHECK_EXPIRATION="SELECT ID FROM POST WHERE  NOW() > enddate AND flag = false ;";
    public static final String SQL_GET_POST_INFORMATION="";
    public static final String SQL_POST_IS_EXPIRED="SELECT COUNT(*) FROM POST WHERE ID= ? AND ENDDATE > NOW() ;";

}
