export default function douglasPeucker(points, epsilon) {
  // 최대 거리를 찾는 함수
  function findPerpendicularDistance(point, lineStart, lineEnd) {
    var dx = lineEnd.x - lineStart.x;
    var dy = lineEnd.y - lineStart.y;

    // 선분이 실제로는 점이라면
    if (dx == 0 && dy == 0) {
      dx = point.x - lineStart.x;
      dy = point.y - lineStart.y;
      return Math.sqrt(dx * dx + dy * dy);
    }

    var t = ((point.x - lineStart.x) * dx + (point.y - lineStart.y) * dy) / (dx * dx + dy * dy);

    t = Math.max(0, Math.min(1, t));

    var closestPoint = [lineStart.x + t * dx, lineStart.y + t * dy];

    dx = point.x - closestPoint[0];
    dy = point.y - closestPoint[1];

    return Math.sqrt(dx * dx + dy * dy);
  }

  // 알고리즘의 메인 함수
  function douglasPeuckerStep(points, firstIndex, lastIndex, epsilon, result) {
    var maxDistance = 0;
    var index = 0;

    for (var i = firstIndex + 1; i < lastIndex; i++) {
      var distance = findPerpendicularDistance(points[i], points[firstIndex], points[lastIndex]);

      if (distance > maxDistance) {
        index = i;
        maxDistance = distance;
      }
    }

    if (maxDistance > epsilon) {
      var recursiveResults1 = douglasPeuckerStep(points, firstIndex, index, epsilon, []);
      var recursiveResults2 = douglasPeuckerStep(points, index, lastIndex, epsilon, []);

      // 병합 결과
      result = recursiveResults1.slice(0, recursiveResults1.length - 1).concat(recursiveResults2);
    } else {
      result = [points[firstIndex], points[lastIndex]];
    }

    return result;
  }

  return douglasPeuckerStep(points, 0, points.length - 1, epsilon, []);
}
