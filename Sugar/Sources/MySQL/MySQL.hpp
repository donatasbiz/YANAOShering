#pragma once

#include <memory>

#include <mysql/jdbc.h>

namespace MySQL
{
	class Connection
	{
	public:
		Connection(const sql::SQLString& t_hostName, const sql::SQLString& t_userName, const sql::SQLString& t_password, const sql::SQLString& t_catalog);

		std::unique_ptr<sql::ResultSet> Query(const sql::SQLString& t_query);
	private:
		sql::mysql::MySQL_Driver* m_driver;
		std::unique_ptr<sql::Connection> m_connection;
	};
}