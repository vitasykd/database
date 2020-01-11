package DBService;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import Confectionary.Workers;
import Confectionary.Addresses;

public class database_connection {
    private static final String connectionName = "jdbc:oracle:thin:@85.254.218.229:1521:DITF11";
    private static final String username = "DB_171RDB220";
    private static final String password = "DB_171RDB220";

    private static final String GET_ALL_WORKERS = "SELECT * FROM workers";
    private static final String INSERT_INTO_WORKERS = "INSERT INTO workers VALUES(?, skolasbiedrs(?, ?, ?, ?, ?, null), ?)";
    private static final String INSERT_INTO_ADDRESSES = "INSERT INTO addresses VALUES(address(?, ?, ?, ?, ?, ?))";
    private static final String INSRT_ADRESE_INTO_WORKERS = "declare\n" +
            "ats REF addresses;\n" +
            "BEGIN\n" +
            "SELECT REF(A) INTO ats FROM addresses A WHERE A.address_id = ?;\n" +
            "UPDATE workers W SET W.workers.address = ats WHERE W.worker_id = ?;\n" +
            "END;";
    private static final String DELETE_WORKERS = "DELETE FROM workers WHERE worker_id=?";


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionName, username, password);
    }


    public static List<Workers> getAllRowsFromWorkers(){
    List<Workers> workerList = new ArrayList<>();
    try (Connection db = getConnection()){
        Statement stm = db.createStatement();
        ResultSet resultSet = stm.executeQuery(GET_ALL_WORKERS);
        while(resultSet.next()){
            Workers worker = new Workers();
            worker.setId(Integer.parseInt(resultSet.getObject(1).toString()));
            worker.setWorkersInfo(getAllRowsFromWorkers());
                worker.setWorkersInfo(((Struct) resultSet.getObject(2)));
                worker.getWorkersInfo().setDzivesVieta(getAdreseFromStruct((Struct) ((Ref)
                        ((Struct) resultSet.getObject(2)).getAttributes()[5]).getObject()));
                workerList.add(worker);
            }

        return workerList;
    } catch (SQLException e){
        System.err.format("SQL Status: %s\n%s", e.getSQLState(), e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    public static boolean insertIntoSkolniekiTable(Workers skolnieks) {
        Connection conn = null;
        try {
            db = getConnection();
            db.setAutoCommit(false);
            PreparedStatement stmtSkolnieks = conn.prepareStatement(INSERT_INTO_WORKERS);

            stmWorkers.setBigDecimal(1, new BigDecimal(skolnieks.getId()));
            stmWorkers.setBigDecimal(2, new BigDecimal(skolnieks.getWorkersInfo().getId()));
            stmWorkers.setString(3, skolnieks.getWorkersInfo().getVards());
            stmWorkers.setString(4, skolnieks.getWorkersInfo().getUzvards());
            stmWorkers.setString(5, skolnieks.getWorkersInfo().getePast());
            stmWorkers.setString(6, skolnieks.getWorkersInfo().getTelefonaNumurs());
            stmWorkers.setBigDecimal(7, new BigDecimal(skolnieks.getKlaseId()));

            stmWorkers.executeUpdate();

            if (Objects.nonNull(skolnieks.getWorkersInfo().getDzivesVieta())) {
                PreparedStatement stmtAdrese = conn.prepareStatement(INSERT_INTO_ADDRESSES);
                CallableStatement cstmtAdrese = conn.prepareCall(INSRT_ADRESE_INTO_SKOLNIEKS);

                stmtAdrese.setBigDecimal(1, new BigDecimal(skolnieks.getWorkersInfo().getDzivesVieta().getId()));
                stmtAdrese.setBigDecimal(2, new BigDecimal(skolnieks.getWorkersInfo().getDzivesVieta().getDzivoklaNumurs()));
                stmtAdrese.setString(3, skolnieks.getWorkersInfo().getDzivesVieta().getIela());
                stmtAdrese.setString(4, skolnieks.getWorkersInfo().getDzivesVieta().getPilseta());
                stmtAdrese.setString(5, skolnieks.getWorkersInfo().getDzivesVieta().getValst());
                stmtAdrese.setString(6, skolnieks.getWorkersInfo().getDzivesVieta().getPastaIndeks());

                stmtAdrese.executeUpdate();

                cstmtAdrese.setBigDecimal(1, new BigDecimal(skolnieks.getWorkersInfo().getDzivesVieta().getId()));
                cstmtAdrese.setBigDecimal(2, new BigDecimal(skolnieks.getId()));

                cstmtAdrese.execute();
            }

            db.commit();
            return true;
        } catch (SQLException e) {
            try {
                Objects.requireNonNull(db).rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.err.format("SQL Status: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            try {
                Objects.requireNonNull(db).rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                Objects.requireNonNull(db).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteWorkers(int id) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_WORKERS);
            stmt.setBigDecimal(1, new BigDecimal(id));
            int res = stmt.executeUpdate();
            return res > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static Addresse getAdreseFromStruct(Struct struct) throws SQLException {
        Addresse address = new Addresse();

        address.setId(Integer.parseInt(struct.getAttributes()[0].toString()));
        address.setCountry(Integer.parseInt(struct.getAttributes()[1].toString()));
        address.setCity(struct.getAttributes()[2].toString());
        address.setStreet(struct.getAttributes()[3].toString());
        address.setPostal_code(struct.getAttributes()[4].toString());

        return address;
    }
}
