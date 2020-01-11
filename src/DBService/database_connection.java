package DBService;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import Confectionary.Addresses;
import Confectionary.Worker;

public class database_connection {
    private static final String connectionName = "jdbc:oracle:thin:@85.254.218.229:1521:DITF11";
    private static final String username = "DB_171RDB220";
    private static final String password = "DB_171RDB220";

    private static final String GET_ALL_WORKERS = "SELECT * FROM workers";
    private static final String INSERT_INTO_WORKERS = "INSERT INTO workers VALUES(?, skolasbiedrs(?, ?, ?, ?, ?, null), ?)";
    private static final String INSERT_INTO_ADDRESSES = "INSERT INTO addresses VALUES(address(?, ?, ?, ?, ?, ?))";
    private static final String INSERT_ADDRESSES_INTO_WORKERS = "declare\n" +
            "ats REF addresses;\n" +
            "BEGIN\n" +
            "SELECT REF(A) INTO ats FROM addresses A WHERE A.address_id = ?;\n" +
            "UPDATE workers W SET W.workers.address = ats WHERE W.worker_id = ?;\n" +
            "END;";
    private static final String DELETE_WORKERS = "DELETE FROM workers WHERE worker_id=?";


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionName, username, password);
    }

    public static boolean insertIntoWorkersTable(Worker worker) {
        Connection db = null;
        try {
            db = getConnection();
            db.setAutoCommit(false);
            PreparedStatement stmWorkers = db.prepareStatement(INSERT_INTO_WORKERS);

            stmWorkers.setBigDecimal(1, new BigDecimal(worker.getWorker_id()));
            stmWorkers.setBigDecimal(2, new BigDecimal(worker.getWorkersInfo().getId()));
            stmWorkers.setString(3, worker.getWorkersInfo().getVards());
            stmWorkers.setString(4, worker.getWorkersInfo().getUzvards());
            stmWorkers.setString(5, worker.getWorkersInfo().getePast());
            stmWorkers.setString(6, worker.getWorkersInfo().getTelefonaNumurs());
            stmWorkers.setBigDecimal(7, new BigDecimal(worker.getKlaseId()));

            stmWorkers.executeUpdate();

            if (Objects.nonNull(worker.getAddress())) {
                PreparedStatement stmtAdrese = db.prepareStatement(INSERT_INTO_ADDRESSES);
                CallableStatement cstmtAdrese = db.prepareCall(INSERT_ADDRESSES_INTO_WORKERS);

                stmtAdrese.setBigDecimal(1, new BigDecimal(worker.getWorkersInfo().getDzivesVieta().getId()));
                stmtAdrese.setBigDecimal(2, new BigDecimal(worker.getWorkersInfo().getDzivesVieta().getDzivoklaNumurs()));
                stmtAdrese.setString(3, worker.getWorkersInfo().getDzivesVieta().getIela());
                stmtAdrese.setString(4, worker.getWorkersInfo().getDzivesVieta().getPilseta());
                stmtAdrese.setString(5, worker.getWorkersInfo().getDzivesVieta().getValst());
                stmtAdrese.setString(6, worker.getWorkersInfo().getDzivesVieta().getPastaIndeks());

                stmtAdrese.executeUpdate();

                cstmtAdrese.setBigDecimal(1, new BigDecimal(worker.getWorkersInfo().getDzivesVieta().getId()));
                cstmtAdrese.setBigDecimal(2, new BigDecimal(worker.getId()));

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

    public static List<Worker> getAllRindasFromWORKERS() {
        List<Worker> workerList = new ArrayList<>();
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(GET_ALL_SKOLNIEKS);
            while (resultSet.next()) {
                Skolnieks skolnieks = new Skolnieks();
                skolnieks.setId(Integer.parseInt(resultSet.getObject(1).toString()));
                skolnieks.setSkolnieksInfo(getSkolasBiedrsFromStruct((Struct) resultSet.getObject(2)));
                skolnieks.getSkolnieksInfo().setDzivesVieta(getAdreseFromStruct((Struct) ((Ref)
                        ((Struct) resultSet.getObject(2)).getAttributes()[5]).getObject()));
                skolnieks.setKlaseId(Integer.parseInt(resultSet.getObject(3).toString()));
                workerList.add(skolnieks);
            }

            return workerList;
        } catch (SQLException e) {
            System.err.format("SQL Status: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
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

    private static Addresses getAdreseFromStruct(Struct struct) throws SQLException {
        Addresses address = new Addresses();

        address.setId(Integer.parseInt(struct.getAttributes()[0].toString()));
        address.setCountry(struct.getAttributes()[1].toString());
        address.setCity(struct.getAttributes()[2].toString());
        address.setStreet(struct.getAttributes()[3].toString());
        address.setPostal_code(struct.getAttributes()[4].toString());

        return address;
    }
}
