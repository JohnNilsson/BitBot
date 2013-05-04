define ['jquery'], ($) ->
  loadTrades: (start, stop, step, callback) ->
    console.log 'Load trades'
    params = $.param
      'start': +start
      'stop': +stop
      'step': +step
    url = 'trades?' + params
    console.log 'Fetching',url
    $.getJSON(url)
      .done (d) -> 
        if(d)
          callback(null, d)
        else
          console.error("Fetch got null")
        return
      .fail (jqxhr, msg, e) ->
        console.error(msg)
        console.error(e)
        return
    return