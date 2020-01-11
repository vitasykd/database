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
    private static final String INSERT_INTO_WORKERS = "INSERT INTO workers(worker_id, name, surname, phone_number, occupation, address)" +
            " VALUES(?, ?, ?, ?, ?, null)";
    private static final String INSERT_INTO_ADDRESSES = "INSERT INTO addresses VALUES(address(?, ?, ?, ?, ?))";
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
            stmWorkers.setString(2, worker.getName());
            stmWorkers.setString(3, worker.getSurname());
            stmWorkers.setBigDecimal(4, new BigDecimal(worker.getPhone_number()));
            stmWorkers.setString(5, worker.getOccupation());

            stmWorkers.executeUpdate();

            if (Objects.nonNull(worker.getAddress())) {
                PreparedStatement stmtAdrese = db.prepareStatement(INSERT_INTO_ADDRESSES);
                CallableStatement cstmtAdrese = db.prepareCall(INSERT_ADDRESSES_INTO_WORKERS);

                stmtAdrese.setBigDecimal(1, new BigDecimal(worker.getAddress().getId()));
                stmtAdrese.setString(2, worker.getAddress().getCountry());
                stmtAdrese.setString(3, worker.getAddress().getCity());
                stmtAdrese.setString(4, worker.getAddress().getStreet());
                stmtAdrese.setString(5, worker.getAddress().getPostal_code());

                stmtAdrese.executeUpdate();

                cstmtAdrese.setBigDecimal(1, new BigDecimal(worker.getAddress().getId()));
                cstmtAdrese.setBigDecimal(2, new BigDecimal(worker.getWorker_id()));

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
            ResultSet resultSet = stmt.executeQuery(GET_ALL_WORKERS);
            while (resultSet.next()) {
                Worker worker = new Worker();
                worker.setWorker_id(Integer.parseInt(resultSet.getObject(1).toString()));
                worker.setName(resultSet.getObject(2).toString());
                worker.setSurname(resultSet.getObject(3).toString());
                worker.setPhone_number(Integer.parseInt(resultSet.getObject(4).toString()));
                worker.setOccupation(resultSet.getObject(4).toString());
                worker.setAddress(getAdreseFromStruct((Struct) ((Ref)
                        ((Struct) resultSet.getObject(3))).getObject()));
                workerList.add(worker);
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
