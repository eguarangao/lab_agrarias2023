package global;

import java.sql.*;

public class Conexion {

    public Connection connection;
    private Statement statement;
    private ResultSet result;
    public boolean estado;

    //Credenciales para la conexion
    private String url = "jdbc:postgresql://10.1.1.204:7878/laboratorios2023uteq";
    private String usuario = "postgres";
    private String clave = "postgres";
    private String classForName = "org.postgresql.Driver";

    public Conexion() {
        estado = true;
    }

    public Conexion(String user, String pass, String url) {
        usuario = user;
        clave = pass;
        this.url = url;
        estado = true;
    }
    //Métodos get y set

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResult() {
        return result;
    }

    public void setResult(ResultSet result) {
        this.result = result;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    //  MÉTODO CONECTAR PARA INICIAR UNA CONEXIÓN A LA BASE DE DATOS
    public boolean conectar() {
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, usuario, clave);
                statement = connection.createStatement();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("No hay conexion a la base de datos: " + e.getMessage());
        }
        return false;
    }

    //  MÉTODO DESCONECTAR PARA CERRAR UNA CONEXIÓN A LA BASE DE DATOS
    public boolean desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                statement.close();
                return true;
            } else {
                System.out.println("No hay una conexion para desconectar");
            }
        } catch (SQLException ex) {
            System.out.println("Hubo un problema al desconectar la conexion");
        }
        return false;
    }

    //  EJECUTAR CONSULTAS SQL
    public ResultSet ejecutarSql(String sql) {
        try {
            conectar();
            result = statement.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("Error: No se ejecuto la consulta: " + ex.getMessage());
        }
        return result;
    }

    public boolean ejecutarSqlBool(int idP) {
        boolean band = false;
        int ret;
        try {
            conectar();
            String sql = "select laboratorio.rol_boolean('" + idP + "');";

            ret = statement.executeUpdate(sql);
            System.out.println(ret);
            band = true;

        } catch (SQLException e) {
            System.out.println("Error: No se ejecuto la consulta: " + e.getMessage());
        }
        return band;
    }
}
