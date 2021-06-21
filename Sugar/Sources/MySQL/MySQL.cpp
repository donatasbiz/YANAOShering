#include "MySQL.hpp"

using namespace MySQL;

Connection::Connection(const sql::SQLString& t_hostName, const sql::SQLString& t_userName,
	const sql::SQLString& t_password, const sql::SQLString& t_catalog)
	: m_driver{ sql::mysql::get_driver_instance() },
	m_connection{ m_driver->connect(t_hostName, t_userName, t_password) }
{
	m_connection->setSchema(t_catalog);
}

std::unique_ptr<sql::ResultSet> Connection::Query(const sql::SQLString& t_query)
{
	std::unique_ptr<sql::Statement> statement{ m_connection->createStatement() };
	statement->execute(t_query);

	return std::unique_ptr<sql::ResultSet>{ statement->getResultSet() };
}