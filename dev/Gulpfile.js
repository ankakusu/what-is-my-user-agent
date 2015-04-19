var gulp  = require('gulp'),
    sass  = require('gulp-sass');

// Standard handler
function standardHandler(err) {
    // Log to console
    console.log(err.message);
    this.emit('end');
}

gulp.task('sass', function() {
    return gulp.src('./scss/style.scss')
        .pipe(sass(({sourcemap: true})))
        .on('error', standardHandler)
        .pipe(gulp.dest('./'));
});

gulp.task('watcher', ['sass'] , function() {
    gulp.watch('./scss/*.scss', ['sass'] );
});

gulp.task('default', ['sass']);