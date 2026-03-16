const COLUMNS = [
  "Candidate", "Type", "Column1", "Exam", "Language",
  "Exam Date", "Score", "Result", "Time Used"
];

function runMP17(rows) {
  const longest = COLUMNS.map((_, ci) =>
    rows.reduce((best, row) => row[ci].length > best.length ? row[ci] : best, '')
  );
  const maxLen = Math.max(...longest.map(v => v.length));
  const wi = longest.findIndex(v => v.length === maxLen);

  let html = `
    <div class="winner-banner">
      <div class="winner-icon">#1</div>
      <div class="winner-text">
        <div class="label">Overall Longest Entry</div>
        <div class="value">${longest[wi] || '(empty)'}</div>
        <div class="meta">${COLUMNS[wi]} · ${maxLen} characters</div>
      </div>
    </div>
    <div class="section-label">Per Column Breakdown</div>
    <div class="col-grid">
  `;

  COLUMNS.forEach((col, ci) => {
    const val = longest[ci];
    const pct = maxLen > 0 ? (val.length / maxLen * 100) : 0;
    html += `
      <div class="col-card ${ci === wi ? 'highlight' : ''}">
        <div class="col-label">${col}</div>
        <div class="col-value">${val || '<em style="color:var(--muted)">empty</em>'}</div>
        <div class="col-bar-wrap"><div class="col-bar" style="width:0%" data-w="${pct}%"></div></div>
        <div class="col-chars">${val.length} chars</div>
      </div>
    `;
  });

  html += '</div>';
  document.getElementById('mp17-results').innerHTML = html;
}