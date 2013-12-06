package es.uvigo.esei.dai.server.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.esei.dai.server.PaginaNotFoundException;
import es.uvigo.esei.dai.server.entity.XSLT;

public class XSLTDBDAO{
	private final Connection connection;

	public XSLTDBDAO(Connection connection) {
		this.connection = connection;
	}
	public void create(XSLT xslt) throws SQLException{
		String insert = "INSERT INTO XSLT " +"(uuid,content,xsd) " + "VALUES (?, ?,?)";

		try (PreparedStatement statement = this.connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1,xslt.getUUID());
			statement.setString(2,xslt.getContent());
			statement.setString(3,xslt.getXSD());

			if(statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar INSERT");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public void update(XSLT xslt) throws PaginaNotFoundException, SQLException{

		String update = "UPDATE XSLT SET uuid=?, content=?, xsd=?";

		try (PreparedStatement statement = this.connection.prepareStatement(update)) {
			statement.setString(1,xslt.getUUID());
			statement.setString(2,xslt.getContent());
			statement.setString(3,xslt.getXSD());
			
			if(statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar UPDATE");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public void delete(String uuid) throws PaginaNotFoundException, SQLException {

		String delete = "DELETE FROM XSLT WHERE uuid=?";
		try (PreparedStatement statement = this.connection.prepareStatement(delete)) {
			statement.setString(1, uuid);
			
			if (statement.executeUpdate() != 1) {
				throw new SQLException("Error al ejecutar DELETE");
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public XSLT get(String uuid) throws PaginaNotFoundException, SQLException {
		String select = "SELECT * FROM XSLT WHERE uuid=?";
		
		try (PreparedStatement statement = this.connection.prepareStatement(select)) {
			statement.setString(1, uuid);
			try(ResultSet rs=statement.executeQuery()){
				rs.next();
				return new XSLT(rs.getString("uuid"),rs.getString("content"), rs.getString("xsd"));
			}catch(SQLException e){
				throw new PaginaNotFoundException("La pagina no existe",uuid);
			}
		} catch (SQLException e) {
			throw e;
		}
	}
	public ArrayList<String> getUUIDS() throws SQLException {
		String select = "SELECT * FROM XSLT";
		
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
	public List<XSLT> list() throws SQLException{
		String select="SELECT * FROM XSLT";
		List<XSLT> lista=new ArrayList<XSLT>();
		
		try(PreparedStatement statement=this.connection.prepareStatement(select)){
			ResultSet rs=statement.executeQuery();
			
			while(rs.next()){
				lista.add(new XSLT(rs.getString("uuid"),rs.getString("content"),rs.getString("xsd")));
			}
			return lista;
			
		}catch (SQLException e){
			throw e;
		}
	}
}
