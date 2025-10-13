package com.busbooking.entity;

import com.busbooking.entity.AppUser;

import java.sql.*;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import java.io.Serializable;
import org.hibernate.HibernateException;

// GenderEnumType
public class GenderEnumType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[] { Types.OTHER };
    }

    @Override
    public Class<String> returnedClass() {
        return String.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        return rs.getString(names[0]);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            // Truyền giá trị kiểu chuỗi, ép kiểu về enum PostgreSQL
            // Ép kiểu về enum PostgreSQL bằng cú pháp ::gender
            st.setObject(index, value + "::gender", Types.OTHER);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || (x != null && x.equals(y));
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }
}

// UserStatusEnumType
class UserStatusEnumType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[] { Types.OTHER };
    }

    @Override
    public Class<String> returnedClass() {
        return String.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws SQLException {
        return rs.getString(names[0]);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            // Ép kiểu về enum PostgreSQL bằng cú pháp ::userstatus
            st.setObject(index, value + "::userstatus", Types.OTHER);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || (x != null && x.equals(y));
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }
}
