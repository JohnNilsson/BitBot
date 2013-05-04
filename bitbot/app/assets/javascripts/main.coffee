requirejs.config
  paths: 
    jquery:   "//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery"
    d3:       "//cdnjs.cloudflare.com/ajax/libs/d3/3.0.8/d3"
    cubism:   "//cdnjs.cloudflare.com/ajax/libs/cubism/1.2.2/cubism.v1"
  shim:
    jquery:
      exports: 'jQuery'
      init: ($) -> $.noConflict(true)
    d3:
      exports: 'd3'
    cubism:
      deps: ['d3']
      exports: 'cubism'

require ['d3', 'cubism', 'plot'], (d3, cubism, p) ->
  console.log("Create context")
  ctxM1 = cubism.context()
    .step(1000*60) # One minute steps
    .size(60*24) # 24 hours of data
  # ctxH1 = cubism.context()
  #   .step(1000*60*60) # One hour steps
  #   .size(24*60) # Two Months of data
  p.plot(ctxM1, d3.select(document.body).append("div"))
  # p.plot(ctxH1, d3.select(document.body).append("div"))
  return