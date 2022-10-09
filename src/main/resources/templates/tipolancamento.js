$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/api/tipolancamento"
    }).then(function(data) {
       $('.greeting-cdTipoLancamento').append(data.id);
       $('.greeting-dtCadastro').append(data.content);
       $('.greeting-dsTipoLancamento').append(data.content);
    });
});