#pragma once

#include <vector>
#include <thread>
#include <locale>
#include <functional>

#include <boost/asio.hpp>
#include <boost/locale.hpp>
#include <boost/locale/utf.hpp>

#include "Server/Listener.hpp"
#include "Utility/Settings.hpp"

class Handler
{
public:
	Handler();

	void Start(std::function<std::string(std::string_view)> t_callback);
	void Stop();
	void Wait();

private:
	std::vector<std::thread> m_threads;
	boost::asio::io_context m_ioc;
	Server::Listener m_listener;
};
