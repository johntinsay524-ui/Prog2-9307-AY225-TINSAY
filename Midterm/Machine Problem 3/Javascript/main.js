function parseLine(line) {
  const f = []; let cur = '', q = false;
  for (const ch of line) {
    if (ch === '"') q = !q;
    else if (ch === ',' && !q) { f.push(cur.trim()); cur = ''; }
    else cur += ch;
  }
  f.push(cur.trim());
  return f;
}

function loadCSV(text) {
  const lines = text.replace(/^\uFEFF/, '').split(/\r?\n/);
  const rows = [];
  const REQ = [0, 1, 3, 4, 5, 6, 7, 8];
  for (let i = 7; i < lines.length; i++) {
    const f = parseLine(lines[i]);
    const p = [...f, ...Array(9).fill('')].slice(0, 9);
    if (REQ.some(idx => p[idx].trim() !== '')) rows.push(p);
  }
  return rows;
}

function animateBars() {
  document.querySelectorAll('[data-w]').forEach(el => {
    setTimeout(() => { el.style.width = el.dataset.w; }, 50);
  });
}

function switchTab(mp) {
  document.querySelectorAll('.tab').forEach((t, i) =>
    t.classList.toggle('active', ['mp17', 'mp18', 'mp19'][i] === mp));
  document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
  document.getElementById('panel-' + mp).classList.add('active');
  setTimeout(animateBars, 50);
}

// File input handling
const csvInput = document.getElementById('csv-input');
const filePathDisplay = document.getElementById('file-path-display');
const loadBtn = document.getElementById('load-btn');

csvInput.addEventListener('change', () => {
  if (csvInput.files.length > 0) {
    filePathDisplay.value = csvInput.files[0].name;
    loadBtn.disabled = false;
  }
});

loadBtn.addEventListener('click', () => {
  const file = csvInput.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = (e) => {
    const rows = loadCSV(e.target.result);

    const banner = document.getElementById('loaded-banner');
    banner.innerHTML = `✅ &nbsp;<strong>${file.name}</strong> &nbsp;— loaded · ${rows.length} records ready`;
    banner.style.display = '';

    document.getElementById('file-prompt').style.display = 'none';
    document.getElementById('main-content').style.display = '';

    runMP17(rows);
    runMP18(rows);
    runMP19(rows);
    setTimeout(animateBars, 150);
  };
  reader.readAsText(file);
});