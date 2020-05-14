/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testedbfiap;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author calixto
 */
public class TesteDBFiap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

            String url = "yoururl";
            String user = "youruser";
            String password = "yourpaswd";

            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setSchema("yourschema");

            if (!conn.isClosed()) {
                System.out.println("Connected!");
            }

            CallableStatement cs = conn.prepareCall("{call todos_cliente(?)}");
            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            
            cs.executeQuery();

            ResultSet rs = cs.getObject(1, ResultSet.class);

            /*while(rs.next()){
                System.out.println(rs.getString("ID_CLIENTE") + " / " + rs.getString("NOME") + " / " +  rs.getString("SOBRENOME") + " / " + rs.getString("OBS") + " / " + rs.getString("DATA_NASC"));
            }*/
            
            JSONArray json = new JSONArray();
            
            
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int numColumns = rsmd.getColumnCount();
                
                JSONObject obj = new JSONObject();

                for (int i = 1; i < numColumns + 1; i++) {
                    String column_name = rsmd.getColumnName(i);

                    switch (rsmd.getColumnType(i)) {
                        case java.sql.Types.ARRAY:
                            obj.put(column_name, rs.getArray(column_name));
                            break;
                        case java.sql.Types.BIGINT:
                            obj.put(column_name, rs.getInt(column_name));
                            break;
                        case java.sql.Types.BOOLEAN:
                            obj.put(column_name, rs.getBoolean(column_name));
                            break;
                        case java.sql.Types.BLOB:
                            obj.put(column_name, rs.getBlob(column_name));
                            break;
                        case java.sql.Types.DOUBLE:
                            obj.put(column_name, rs.getDouble(column_name));
                            break;
                        case java.sql.Types.FLOAT:
                            obj.put(column_name, rs.getFloat(column_name));
                            break;
                        case java.sql.Types.INTEGER:
                            obj.put(column_name, rs.getInt(column_name));
                            break;
                        case java.sql.Types.NVARCHAR:
                            obj.put(column_name, rs.getNString(column_name));
                            break;
                        case java.sql.Types.VARCHAR:
                            obj.put(column_name, rs.getString(column_name));
                            break;
                        case java.sql.Types.TINYINT:
                            obj.put(column_name, rs.getInt(column_name));
                            break;
                        case java.sql.Types.SMALLINT:
                            obj.put(column_name, rs.getInt(column_name));
                            break;
                        case java.sql.Types.DATE:
                            obj.put(column_name, rs.getDate(column_name));
                            break;
                        case java.sql.Types.TIMESTAMP:
                            obj.put(column_name, rs.getTimestamp(column_name));
                            break;
                        default:
                            obj.put(column_name, rs.getObject(column_name));
                            break;
                    }
                }

                json.put(obj);
            }
            
            System.out.println(json);
            
            cs.close();
            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(TesteDBFiap.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
