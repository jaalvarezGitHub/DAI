package es.uvigo.esei.dai.server.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.entity.XML;

public class XMLDBDAO{
	private final Connection connection;

	public XMLDBDAO(Connection connection) {
		this.connection = connection;
	}
	public void create(XML pagina) throws SQLException{
		String insert = "INSERT INTO XML_ " +"(uuid,content) " + "VALUES (?, ?)";

		try (PreparedStatement statement = this.connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1,pagina.getUUID());
			statement.setString(2,pagina.getContent());

			if(statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar INSERT");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public void update(XML pagina) throws PaginaNotFoundException, SQLException{

		String update = "UPDATE XML_ SET uuid=?, content=?";

		try (PreparedStatement statement = this.connection.prepareStatement(update)) {
			statement.setString(1,pagina.getUUID());
			statement.setString(2,pagina.getContent());

			if(statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar UPDATE");
			}
		} catch (SQLException e) {
			throw e;
		}

	}
	public void delete(String uuid) throws PaginaNotFoundException, SQLException {

		String delete = "DELETE FROM XML_ WHERE uuid=?";
		try (PreparedStatement statement = this.connection.prepareStatement(delete)) {
			
			statement.setString(1, uuid);
			
			if (statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar DELETE");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public XML get(String uuid) throws PaginaNotFoundException, SQLException {
		String select = "SELECT * FROM XML_ WHERE uuid=?";
		
		try (PreparedStatement statement = this.connection.prepareStatement(select)) {
			statement.setString(1, uuid);
			try(ResultSet rs=statement.executeQuery()){
				rs.next();
				return new XML(rs.getString("uuid"),rs.getString("content"));
			}catch(SQLException e){
				throw new PaginaNotFoundException("La pagina no existe",uuid);
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public ArrayList<String> getUUIDS() throws SQLException {
		String select = "SELECT * FROM XML_";
		try(PreparedStatement statement=this.connection.prepareStatement(select)){
					ResultSet rs=statement.executeQuery();
						ArrayList<String> result=new ArrayList<String>();
					    while(rs.next()){
								result.add(rs.getString("uuid"));
						}
						return result;		
			}catch (SQLException e) {
				throw e;
			}
}
	public List<XML> list() throws SQLException{
		String select="SELECT * FROM XML_";
		List<XML> lista=new ArrayList<XML>();
		
		try(PreparedStatement statement=this.connection.prepareStatement(select)){
			ResultSet rs=statement.executeQuery();
			
			while(rs.next()){
				lista.add(new XML(rs.getString("uuid"),rs.getString("content")));
			}
			return lista;
			
		}catch (SQLException e){
			throw e;
		}
	}
}
