const $ = id => document.getElementById(id);
const CUR = {France: "€", India: "₹", Italy: "€", Japan: "¥", UK: "£"};
const CROWD = ["", "🟢 Low crowd", "🟡 Moderate", "🟠 Busy", "🔴 Very busy"];
const TAGS = ["history", "art", "landmark", "nature", "quiet"];
const st = {country: "France", start: 0, hrs: 5, t0: 9, tags: {history: 1, art: 1, landmark: 1}, skip: new Set(), list: [], plan: null, sel: null};
const map = L.map("map");
L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {attribution: "© OpenStreetMap"}).addTo(map);
const layer = L.layerGroup().addTo(map);
let me = null, timer = null, watchId = null;

const clk = m => String(Math.floor(m / 60) % 24).padStart(2, "0") + ":" + String(m % 60).padStart(2, "0");
const fmt = m => { const h = Math.floor(m / 60), r = m % 60; return (h ? h + "h " : "") + (r || !h ? r + "m" : ""); };
const fee = a => a.fee ? CUR[st.country] + a.fee.toLocaleString() : "Free";
const api = async path => { const r = await fetch(path); if (!r.ok) throw new Error(r.status); return r.json(); };
function el(tag, cls, text) { const e = document.createElement(tag); if (cls) e.className = cls; if (text !== undefined) e.textContent = text; return e; }

async function setCountry(c) {
  stopMove(); st.country = c; st.start = 0; st.skip.clear(); st.sel = null;
  st.list = await api("/api/attractions?country=" + encodeURIComponent(c));
  $("start").innerHTML = ""; st.list.forEach(a => $("start").add(new Option(a.name, a.id)));
  refresh();
}

async function refresh() {
  $("hv").textContent = st.hrs + " h";
  const q = new URLSearchParams({country: st.country, start: st.start, hrs: st.hrs, t0: st.t0,
    tags: TAGS.filter(g => st.tags[g]).join(","), skip: [...st.skip].join(",")});
  try { st.plan = await api("/api/plan?" + q); } catch (e) { $("note").textContent = "Could not reach the planning server."; return; }
  draw();
}

function route() { return [st.list[st.start], ...st.plan.stops.map(s => st.list[s.id])].map(a => [a.lat, a.lon]); }

function draw() {
  const p = st.plan, ids = {}; p.stops.forEach((s, i) => ids[s.id] = i + 1);
  layer.clearLayers();
  st.list.forEach(a => {
    const icon = ids[a.id] ? L.divIcon({className: "pin", html: ids[a.id], iconSize: [26, 26]}) : null;
    const m = icon ? L.marker([a.lat, a.lon], {icon}) : L.circleMarker([a.lat, a.lon], {radius: 6, color: "#8F917C", fillOpacity: .9});
    m.bindTooltip(a.name).on("click", () => { st.sel = a.id; info(); }).addTo(layer);
  });
  if (p.stops.length) L.polyline(route(), {color: "#7d93b8", dashArray: "6 6"}).addTo(layer);
  map.fitBounds(st.list.map(a => [a.lat, a.lon]), {padding: [30, 30]});
  if (st.sel === null && p.stops.length) st.sel = p.stops[0].id;
  const s = p.summary;
  $("sum").innerHTML = "";
  [[p.stops.length, "stops"], [fmt(s.used), "planned"], [fmt(Math.max(s.left, 0)), "spare"], [CUR[st.country] + s.cost.toLocaleString(), "entry fees"], [clk(s.fin), "finish"]]
    .forEach(([v, k]) => { const d = el("div"); d.append(el("b", "", v), k); $("sum").append(d); });
  const list = $("list"); list.innerHTML = "";
  if (!p.stops.length) list.append(el("div", "meta", "No attractions fit. Add time or pick more interests."));
  p.stops.forEach((x, i) => {
    const a = st.list[x.id], row = el("div", "stopitem"), t = el("div", "t"), skip = el("button", "x", "×");
    t.append(el("b", "", a.name), el("div", "meta", `Travel ${x.travel} min · 🕒 ${clk(x.at)}–${clk(x.at + a.duration)} (closes ${clk(a.close)}) · ${CROWD[a.crowd]} · 🎟️ ${fee(a)}` + (x.waitMin > 0 ? ` · waits ${x.waitMin} min for opening` : "")));
    t.onclick = () => { st.sel = a.id; info(); };
    skip.setAttribute("aria-label", "Skip " + a.name); skip.onclick = () => { st.skip.add(a.id); refresh(); };
    row.append(el("div", "num", i + 1), t, skip); list.append(row);
  });
  const n = $("note"); n.textContent = "";
  if (st.skip.size) { n.append(st.skip.size + " skipped · "); const b = el("button", "btn", "Restore all"); b.onclick = () => { st.skip.clear(); refresh(); }; n.append(b); }
  info();
}

function info() {
  const box = $("info"); box.innerHTML = ""; box.append(el("h2", "", "Tourist information"));
  const a = st.sel !== null ? st.list[st.sel] : null;
  if (!a) { box.append(el("div", "meta", "Tap a place on the map or in your itinerary to see its details.")); return; }
  box.append(el("h3", "", a.name), el("div", "meta", a.tags.join(" · ")));
  const f = el("div", "facts");
  [["🕒 Hours", clk(a.open) + "–" + clk(a.close)], ["👥 Crowd", CROWD[a.crowd]], ["🎟️ Entry fee", fee(a)], ["⏱️ Visit time", fmt(a.duration)]]
    .forEach(([k, v]) => { const d = el("div", "fb", k); d.append(el("b", "", v)); f.append(d); });
  box.append(f);
  box.append(el("h4", "sec", "History"), el("p", "para", a.history));
  box.append(el("h4", "sec", "Plan your visit"), el("p", "para", visitTip(a)));
  const ff = el("div", "fact"); ff.append(el("b", "", "🎉 Fun facts"), el("div", "", "• " + a.fact), el("div", "", "• " + a.fact2)); box.append(ff);
  box.append(el("div", "meta", "Typical hours and prices. Confirm before visiting."));
}

function visitTip(a) {
  const best = a.open === 0 ? "early morning or late evening" : a.crowd >= 4 ? "right at opening (" + clk(a.open) + ") or in the last hour before closing" : a.crowd === 3 ? "early morning or late afternoon" : "any time of day";
  return "Best time: " + best + ". Allow about " + fmt(a.duration) + ". Good for: " + a.tags.join(", ") + ". " + (a.fee ? "Entry costs about " + fee(a) + "." : "Entry is free.");
}

function marker(ll) {
  if (!me) me = L.marker(ll, {icon: L.divIcon({className: "me", iconSize: [18, 18]})}).addTo(map); else me.setLatLng(ll);
}
function stopMove() {
  clearInterval(timer); timer = null;
  if (watchId !== null) { navigator.geolocation.clearWatch(watchId); watchId = null; }
  if (me) { map.removeLayer(me); me = null; }
  $("gps").textContent = "Follow my GPS";
}
function demo() {
  stopMove();
  if (!st.plan || st.plan.stops.length < 1) { $("msg").textContent = "Plan at least one stop first."; return; }
  const r = route(); let seg = 0, k = 0; const N = 60;
  timer = setInterval(() => {
    const a = r[seg], b = r[seg + 1], f = k / N;
    marker([a[0] + (b[0] - a[0]) * f, a[1] + (b[1] - a[1]) * f]);
    if (++k > N) { k = 0; if (++seg >= r.length - 1) clearInterval(timer); }
  }, 50);
}
function gps() {
  if (watchId !== null) { stopMove(); return; }
  if (!navigator.geolocation) { $("msg").textContent = "Location is not available in this browser."; return; }
  $("msg").textContent = "Waiting for your location…"; $("gps").textContent = "■ Stop";
  watchId = navigator.geolocation.watchPosition(
    p => { marker([p.coords.latitude, p.coords.longitude]); map.panTo([p.coords.latitude, p.coords.longitude]); $("msg").textContent = "Live GPS is on."; },
    e => { stopMove(); $("msg").textContent = e.code === 1 ? "Location access was blocked. Allow it for this page." : "Could not get your location."; },
    {enableHighAccuracy: true, maximumAge: 1000, timeout: 20000});
}

// setup
(async () => {
  const c = await api("/api/countries");
  c.forEach(x => $("ctry").add(new Option(x, x)));
  for (let h = 6; h <= 14; h++) $("t0").add(new Option(String(h).padStart(2, "0") + ":00", h));
  $("t0").value = st.t0;
  TAGS.forEach(g => {
    const b = el("button", "chip" + (st.tags[g] ? " on" : ""), g === "quiet" ? "Avoid crowds" : g[0].toUpperCase() + g.slice(1));
    b.onclick = () => { st.tags[g] = st.tags[g] ? 0 : 1; b.classList.toggle("on"); refresh(); };
    $("chips").append(b);
  });
  $("ctry").onchange = e => setCountry(e.target.value);
  $("start").onchange = e => { st.start = +e.target.value; refresh(); };
  $("hrs").oninput = e => { st.hrs = +e.target.value; refresh(); };
  $("t0").onchange = e => { st.t0 = +e.target.value; refresh(); };
  $("gps").onclick = gps; $("demo").onclick = demo;
  setCountry(st.country);
})();
