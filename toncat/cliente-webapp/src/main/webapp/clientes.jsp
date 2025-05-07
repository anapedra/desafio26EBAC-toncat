<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Lista de Clientes</title>
</head>
<body>
    <h1>Clientes Cadastrados</h1>
    <ul>
        <c:forEach var="cliente" items="${clientes}">
            <li>${cliente.nome} - ${cliente.email}</li>
        </c:forEach>
    </ul>
    <a href="index.jsp">Voltar</a>
</body>
</html>
