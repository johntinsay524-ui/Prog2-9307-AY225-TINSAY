const zone = document.getElementById('upload-zone');
const csvInput = document.getElementById('csv-input');

// ── Drag & drop ──────────────────────────────────────────────────────────────
zone.addEventListener('dragover', e => { e.preventDefault(); zone.classList.add('drag'); });
zone.addEventListener('dragleave', () => zone.classList.remove('drag'));
zone.addEventListener('drop', e => {
  e.preventDefault(); zone.classList.remove('drag');
  const f = e.dataTransfer.files[0];
  if (f && f.name.endsWith('.csv')) processFile(f);
  else alert('Please drop a .csv file.');
});
csvInput.addEventListener('change', () => {
  if (csvInput.files[0]) processFile(csvInput.files[0]);
});

// ── CSV Parsing ───────────────────────────────────────────────────────────────
function parseCSV(text) {
  const lines = text.split(/\r?\n/).filter(l => l.trim());
  if (lines.length < 2) return [];

  const records = [];
  for (let i = 1; i < lines.length; i++) {
    const col = lines[i].split(',');
    if (col.length < 8) continue;

    const salesStr = col[7].trim();
    if (!salesStr) continue;

    const sales = parseFloat(salesStr);
    if (isNaN(sales)) continue;

    records.push({
      title:     col[1].trim(),
      console:   col[2].trim(),
      genre:     col[3].trim(),
      publisher: col[4].trim(),
      sales,
      segment:   classify(sales)
    });
  }
  return records;
}

function classify(s) {
  if (s > 10) return 'Platinum';
  if (s >= 5) return 'Gold';
  if (s >= 1) return 'Silver';
  return 'Bronze';
}

// ── File Handling ─────────────────────────────────────────────────────────────
function processFile(file) {
  document.getElementById('file-name').textContent = file.name;
  const reader = new FileReader();
  reader.onload = e => render(parseCSV(e.target.result), file.name);
  reader.readAsText(file);
}

// ── Render Results ────────────────────────────────────────────────────────────
function render(records, filename) {
  const buckets = { Platinum: [], Gold: [], Silver: [], Bronze: [] };
  records.forEach(r => buckets[r.segment].push(r));
  Object.values(buckets).forEach(b => b.sort((a, c) => c.sales - a.sales));

  document.getElementById('results').style.display = 'block';

  // Stat cards
  animateCount('cnt-plat', buckets.Platinum.length);
  animateCount('cnt-gold', buckets.Gold.length);
  animateCount('cnt-silv', buckets.Silver.length);
  animateCount('cnt-brnz', buckets.Bronze.length);

  // Bars
  const maxCount = Math.max(...Object.values(buckets).map(b => b.length));
  setTimeout(() => {
    setBar('bar-plat', 'bar-cnt-plat', buckets.Platinum.length, maxCount);
    setBar('bar-gold', 'bar-cnt-gold', buckets.Gold.length, maxCount);
    setBar('bar-silv', 'bar-cnt-silv', buckets.Silver.length, maxCount);
    setBar('bar-brnz', 'bar-cnt-brnz', buckets.Bronze.length, maxCount);
  }, 100);

  // Tables
  const container = document.getElementById('seg-tables');
  container.innerHTML = '';
  [
    { key: 'Platinum', cls: 'plat', valCls: 'plat-val', range: '> 10 million sales' },
    { key: 'Gold',     cls: 'gold', valCls: 'gold-val', range: '5M – 10M sales' },
    { key: 'Silver',   cls: 'silv', valCls: 'silv-val', range: '1M – 4.99M sales' },
    { key: 'Bronze',   cls: 'brnz', valCls: 'brnz-val', range: '< 1 million sales' },
  ].forEach(seg => buildTable(container, seg, buckets[seg.key]));

  document.getElementById('footer-info').textContent =
    `${filename} · ${records.length.toLocaleString()} records`;
}

// ── Helpers ───────────────────────────────────────────────────────────────────
function animateCount(id, target) {
  const el = document.getElementById(id);
  const dur = 600, start = performance.now();
  function step(now) {
    const t = Math.min((now - start) / dur, 1);
    el.textContent = Math.round(t * target).toLocaleString();
    if (t < 1) requestAnimationFrame(step);
  }
  requestAnimationFrame(step);
}

function setBar(fillId, countId, count, max) {
  document.getElementById(fillId).style.width = (count / max * 100) + '%';
  document.getElementById(countId).textContent = count.toLocaleString();
}

// ── Table Building ────────────────────────────────────────────────────────────
const PREVIEW = 20;

function buildTable(container, seg, items) {
  const section = document.createElement('div');
  section.className = 'seg-section';
  section.innerHTML = `
    <div class="seg-header">
      <span class="seg-badge ${seg.cls}">${seg.key}</span>
      <span class="seg-meta">${seg.range}</span>
      <span class="seg-total">${items.length.toLocaleString()} titles</span>
    </div>
    <table>
      <thead><tr>
        <th>#</th><th>Title</th><th>Console</th>
        <th>Genre</th><th>Publisher</th><th>Sales (M)</th>
      </tr></thead>
      <tbody id="tbody-${seg.key}"></tbody>
    </table>`;
  container.appendChild(section);

  const tbody = section.querySelector(`#tbody-${seg.key}`);
  renderRows(tbody, items, 0, PREVIEW, seg.valCls);

  if (items.length > PREVIEW) {
    const btn = document.createElement('button');
    btn.className = 'show-more-btn';
    let shown = PREVIEW;
    btn.textContent = `Show more (${(items.length - shown).toLocaleString()} remaining)`;
    btn.onclick = () => {
      renderRows(tbody, items, shown, shown + 50, seg.valCls);
      shown = Math.min(shown + 50, items.length);
      if (shown >= items.length) btn.remove();
      else btn.textContent = `Show more (${(items.length - shown).toLocaleString()} remaining)`;
    };
    section.appendChild(btn);
  }
}

function renderRows(tbody, items, from, to, valCls) {
  for (let i = from; i < Math.min(to, items.length); i++) {
    const r = items[i];
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td style="color:var(--muted)">${i + 1}</td>
      <td class="title-cell" title="${esc(r.title)}">${esc(r.title)}</td>
      <td><span class="console-tag">${esc(r.console)}</span></td>
      <td style="color:var(--muted)">${esc(r.genre)}</td>
      <td>${esc(r.publisher)}</td>
      <td><span class="sales-val ${valCls}">${r.sales.toFixed(2)}</span></td>`;
    tbody.appendChild(tr);
  }
}

function esc(s) {
  return String(s)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}
