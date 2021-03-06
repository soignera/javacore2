package lesson24.touragency.user.repo.jdbc;

import lesson24.touragency.common.business.exception.jdbc.KeyGenerationError;
import lesson24.touragency.common.business.exception.jdbc.SqlError;
import lesson24.touragency.common.solution.repo.jdbc.QueryWrapper;
import lesson24.touragency.user.domain.User;
import lesson24.touragency.user.repo.UserRepo;
import lesson24.touragency.user.search.UserSearchCondition;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDefaultRepoImpl implements UserRepo {
    @Override
    public List<? extends User> search(UserSearchCondition searchCondition) {
        return null;
    }

    @Override
    public User add(User user) {
        try {
            Optional<Long> generatedId = QueryWrapper.executeUpdateReturningGeneratedKey(getInsertUserSql(),
                    ps -> {
                        appendPreparedStatementParamsToUser(new AtomicInteger(0), ps, user);
                    },
                    rs -> rs.getLong("ID"));

            if (generatedId.isPresent()) {
                user.setId(generatedId.get());
            } else {
                throw new KeyGenerationError("ID");
            }

            return user;
        } catch (KeyGenerationError e) {
            throw e;
        } catch (Exception e) {
            throw new SqlError(e);
        }
    }

    private String getInsertUserSql() {
        return "INSERT INTO USER (" +
                "FIRST_NAME," +
                "LAST_NAME," +
                "PASSPORT_NUMBER," +
                "CLIENT_TYPE" +
                ")" +
                "VALUES (?, ? ,? ,?)";
    }

    private void appendPreparedStatementParamsToUser(AtomicInteger index, PreparedStatement ps, User user) throws SQLException {
        ps.setString(index.incrementAndGet(), user.getFirstName());
        ps.setString(index.incrementAndGet(), user.getLastName());
        ps.setInt(index.incrementAndGet(), user.getPassportNumber());
        ps.setString(index.incrementAndGet(), user.getClientType().toString());
    }

    @Override
    public void add(Collection<User> users) {
        try {
            QueryWrapper.executeUpdateAsBatch(getInsertUserSql(), users,
                    (ps, user) -> appendPreparedStatementParamsToUser(new AtomicInteger(0), ps, user));
        } catch (Exception e) {
            throw new SqlError(e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE USER SET " +
                "FIRST NAME = ?," +
                "LAST_NAME = ?, " +
                "PASSPORT_NUMBER = ?, " +
                "CLIENT_TYPE = ? " +
                "WHERE ID = ?";

        try {
            QueryWrapper.executeUpdate(sql,
                    ps -> {
                        AtomicInteger index = new AtomicInteger(0);
                        appendPreparedStatementParamsToUser(index, ps, user);
                        ps.setLong(index.incrementAndGet(), user.getId());
                    });
        } catch (Exception e) {
            throw new SqlError(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return QueryWrapper.selectOne("SELECT * FROM USER WHERE ID = ?",
                    UserMapper::mapUser, ps -> {
                        ps.setLong(1, id);
                    });
        } catch (Exception e) {
            throw new SqlError(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            QueryWrapper.executeUpdate("DELETE FROM USER WHERE ID = ?", ps -> {
                ps.setLong(1, id);
            });
        } catch (Exception e) {
            throw new SqlError(e);
        }
    }

    @Override
    public void printAll() {
        findAll().forEach(System.out::println);
    }

    @Override
    public List<User> findAll() {
        try {
            return QueryWrapper.select("SELECT * FROM USER", UserMapper::mapUser);
        } catch (Exception e) {
            throw new SqlError(e);
        }
    }

    @Override
    public int countAll() {
        try {
            return QueryWrapper.selectOne("SELECT COUNT(*) AS CNT FROM USER",
                    rs -> rs.getInt("CNT")).orElse(0);
        } catch (Exception e) {
            throw new SqlError(e);
        }

    }}
