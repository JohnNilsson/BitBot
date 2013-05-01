loadTrades = (start, stop, step, callback) ->
  console.log 'Load trades'
  params = $.param
    'start': +start
    'stop': +stop
    'step': +step
  url = 'trades?' + params
  console.log 'Fetching',url
  $.getJSON(url)
    .done (d) -> 
      callback(null, d)
      return
    .fail (jqxhr, msg, e) ->
      console.error(e)
      callback(e, null)
      return
  return


requirejs.config
  paths: 
    jQuery:   "//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min"
    d3:       "//cdnjs.cloudflare.com/ajax/libs/d3/3.0.8/d3.min"
    cubism:   "//cdnjs.cloudflare.com/ajax/libs/cubism/1.2.2/cubism.v1.min"
  shim:
    d3:
      exports: 'd3'
    cubism:
      deps: ['d3']
      exports: 'cubism'


require ['jQuery','d3', 'cubism'], ($, d3, cubism) ->
  ctx = cubism.context()
    .step(1000*60) # One minute steps
    .size(60*24) # 24 hours of data

  d3.select(document.body).call (body) ->
    body.datum(ctx.metric(loadTrades))
    
    body
     .append("div")
     .attr("class","axis")
     .call(ctx.axis()
      .tickFormat(d3.time.format("%X"))
      .ticks(d3.time.minutes, 24*4))

    body
      .append("div")
      .attr("class", "horizon")
      .call(ctx.horizon()
        #.height(150)
        .format(d3.format(".2f"))
        .title("$"))


    return
  return