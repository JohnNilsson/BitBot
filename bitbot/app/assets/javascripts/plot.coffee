define ['d3', 'cubism', 'bitbot'], (d3, cubism, bb) ->
  console.log("Define plot")
  plot: (ctx, selection) ->
    console.log("plot")
    selection.call (e) ->
      e.datum(ctx.metric(bb.loadTrades))
      
      e.append("div")
       .attr("class","axis")
       .call(ctx.axis()
        .orient("top")
        .tickFormat(d3.time.format("%X")))

      e.append("div")
       .attr("class", "horizon")
       .call(ctx.horizon()
          .height(150)
          .format(d3.format(".2f"))
          .title("$"))
    return