package com.quizgame.repository;

import com.quizgame.dto.QuizCategoryResponse;
import com.quizgame.model.Question;

import java.util.Comparator;
import java.util.List;

public class QuestionCatalog {

    private static final int QUESTIONS_PER_QUIZ = 15;
    private static final List<Question> QUESTIONS = createQuestions();

    private static List<Question> createQuestions() {
        List<Question> questions = new java.util.ArrayList<>();

        addSet(questions, "JAVA", "EASY",
                q("Which keyword declares a class in Java?", "class", "struct", "define", "type", "The class keyword declares a Java class."),
                q("Which method is the standard Java application entry point?", "public static void main(String[] args)", "public void start()", "static int run()", "private main()", "The JVM starts a standard application by invoking public static void main."),
                q("Which primitive type stores true or false?", "boolean", "byte", "char", "float", "The boolean primitive stores either true or false."),
                q("Which symbol ends most Java statements?", ";", ":", ".", ",", "A semicolon terminates most Java statements."),
                q("Which keyword creates an object from a class?", "new", "make", "create", "object", "The new operator creates an object and invokes a constructor."),
                q("Which type stores a whole number such as 42?", "int", "boolean", "char", "String", "The int primitive stores signed whole numbers."),
                q("Which operator adds two numeric values?", "+", "&", "=", "%", "The plus operator performs numeric addition."),
                q("Which Java type represents text as an object?", "String", "Text", "char[] only", "word", "String is the standard class for text values."),
                q("Which keyword selects between alternatives in a condition?", "if", "when", "choose", "caseof", "An if statement conditionally executes a block."),
                q("Which loop checks its condition before each iteration?", "while", "do-while only", "switch", "try", "A while loop tests its condition before entering each iteration."),
                q("Which access modifier makes a member available from any class?", "public", "private", "protected", "default", "A public member is accessible wherever its containing type is accessible."),
                q("Which keyword declares a constant variable reference?", "final", "fixed", "const", "immutable", "A final variable can be assigned once."),
                q("Which method prints text followed by a line break?", "System.out.println()", "System.in.read()", "Console.scan()", "print.line()", "println writes output and then advances to a new line."),
                q("Which keyword returns a value from a method?", "return", "yield", "send", "break", "return ends a method and may provide its result."),
                q("Which file extension is used for Java source files?", ".java", ".class", ".jar", ".jav", "Java source code files use the .java extension."));

        addSet(questions, "JAVA", "MEDIUM",
                q("Which keyword establishes class inheritance in Java?", "extends", "implements", "inherits", "super", "A class extends a superclass."),
                q("Which keyword declares that a class fulfills an interface?", "implements", "extends", "instanceof", "interface", "A class uses implements to fulfill an interface contract."),
                q("Which collection interface does not permit duplicate elements?", "Set", "List", "Deque", "Map.Entry", "Set models a collection of unique elements."),
                q("Which collection preserves insertion order and permits duplicates?", "ArrayList", "HashSet", "TreeSet", "EnumSet", "ArrayList is an ordered list that permits duplicate values."),
                q("What does a constructor primarily do?", "Initializes a new object", "Destroys an object", "Imports a package", "Starts a thread", "A constructor initializes an instance when it is created."),
                q("Which keyword refers to the current object?", "this", "self", "current", "that", "this refers to the current instance."),
                q("Which keyword refers to a superclass member or constructor?", "super", "base", "parent", "this", "super accesses superclass members and constructors."),
                q("Which exception category must generally be caught or declared?", "Checked exception", "Error only", "Unchecked exception", "Runtime warning", "Checked exceptions are subject to compile-time handling requirements."),
                q("Which block is used for cleanup whether or not an exception occurs?", "finally", "final", "catch", "throws", "A finally block runs after try/catch processing in normal control flow."),
                q("Which keyword prevents a method from being overridden?", "final", "static", "private only", "native", "A final method cannot be overridden by a subclass."),
                q("What does method overloading require?", "Same name with different parameter lists", "Same name and identical parameters", "Different class names", "A return type only", "Overloads share a name but differ in parameter lists."),
                q("Which statement exits the current loop immediately?", "break", "continue", "return only", "pass", "break terminates the nearest loop or switch."),
                q("Which statement skips to the next loop iteration?", "continue", "break", "skip", "next", "continue advances execution to the next iteration."),
                q("Which interface is the usual target for a lambda expression?", "A functional interface", "A marker interface", "A class with two abstract methods", "An enum", "A functional interface has one abstract method and can be implemented by a lambda."),
                q("What does the static keyword indicate for a field?", "It belongs to the class rather than each instance", "It cannot be read", "It is always constant", "It is local to a method", "A static field is shared at the class level."));

        addSet(questions, "JAVA", "HARD",
                q("Which Java memory area stores objects created at runtime?", "Heap", "Java stack frame", "Program counter register", "Method parameter list", "Objects are allocated in the heap; references may live in stack frames."),
                q("What is the main purpose of the Java Memory Model?", "Define visibility and ordering of shared-memory actions", "Specify HTML rendering", "Manage Maven dependencies", "Define SQL transactions", "The Java Memory Model specifies how threads interact through memory."),
                q("Which happens-before rule applies when a thread unlocks a monitor?", "The unlock happens-before a later lock on the same monitor", "Unlocks have no ordering effect", "It orders unrelated network packets", "It applies only to local variables", "Monitor unlock and subsequent lock on the same monitor establish happens-before."),
                q("What does a volatile write guarantee to a later read of the same field?", "Visibility of the latest write under the memory model", "Atomicity for every compound operation", "Mutual exclusion for all methods", "Persistence after process exit", "Volatile writes are visible to subsequent reads of that field."),
                q("Why can double-checked locking require a volatile reference?", "To prevent unsafe publication and reordering", "To make constructors private", "To allow multiple inheritance", "To disable garbage collection", "Volatile prevents publication reordering for the shared instance reference."),
                q("What is type erasure in Java generics?", "Generic type information is mostly removed at compile time", "Every generic becomes a primitive", "Types are checked only at runtime", "Generics are converted to arrays", "The compiler erases most generic type parameters in bytecode."),
                q("Which collection is designed for concurrent access with high read concurrency?", "ConcurrentHashMap", "HashMap", "TreeMap", "IdentityHashMap", "ConcurrentHashMap supports concurrent operations without synchronizing the whole map."),
                q("What does ThreadLocal provide?", "A separate value per thread", "A global lock", "A shared immutable value", "A thread pool", "ThreadLocal associates a value independently with each thread."),
                q("Which operation atomically updates a value based on its current value?", "AtomicInteger.updateAndGet", "Integer.parseInt", "Thread.sleep", "Object.notify", "AtomicInteger update methods perform atomic read-modify-write operations."),
                q("What is a common consequence of acquiring locks in inconsistent orders?", "Deadlock", "Type erasure", "Stack overflow only", "Class initialization", "Inconsistent lock order can create circular waits and deadlock."),
                q("Which mechanism lets a thread wait until another thread signals a condition?", "Condition await and signal", "Thread.yield only", "System.gc", "Class.forName", "Condition provides await/signal coordination when used with a lock."),
                q("What is the contract between equals and hashCode?", "Equal objects must have equal hash codes", "Different objects must always have different hash codes", "hashCode must be unique", "equals must compare identity only", "The Object contract requires equal objects to return the same hash code."),
                q("What does try-with-resources guarantee for an AutoCloseable resource?", "It is closed automatically after the try block", "It is never closed", "It is closed only after a catch block", "It becomes static", "Resources declared in try-with-resources are closed automatically."),
                q("What is the purpose of a sealed class?", "Restrict which classes may extend or implement it", "Prevent object creation", "Make every method final", "Enable reflection", "Sealed types explicitly control their permitted direct subtypes."),
                q("Which statement about records is correct?", "They provide a concise form for transparent data carriers", "They are mutable by default", "They cannot implement interfaces", "They are a kind of annotation", "Records are concise classes intended to model data with generated accessors and methods."));

        addSet(questions, "HTML", "EASY",
                q("Which element represents the top-level heading?", "<h1>", "<h6>", "<header>", "<title>", "h1 is the highest heading level."),
                q("Which attribute gives an anchor its destination URL?", "href", "src", "action", "target", "The href attribute provides a hyperlink destination."),
                q("Which element creates an unordered list?", "<ul>", "<ol>", "<li>", "<list>", "ul represents an unordered list."),
                q("Which element creates a hyperlink?", "<a>", "<link>", "<href>", "<navlink>", "The a element creates a hyperlink when it has an href."),
                q("Which element embeds an image?", "<img>", "<image>", "<picture-src>", "<media>", "The img element embeds an image resource."),
                q("Which attribute provides alternative text for an image?", "alt", "title", "label", "description", "The alt attribute provides a text alternative for an image."),
                q("Which element creates a line break?", "<br>", "<hr>", "<break>", "<lb>", "br inserts a line break."),
                q("Which element represents a paragraph?", "<p>", "<para>", "<text>", "<bodytext>", "The p element represents a paragraph."),
                q("Which element contains visible page content?", "<body>", "<head>", "<meta>", "<title>", "The body element contains the document's visible content."),
                q("Which element sets the document title shown in the browser tab?", "<title>", "<h1>", "<caption>", "<label>", "The title element sets the document title in the head."),
                q("Which attribute provides a unique identifier for an element?", "id", "class", "style", "name-only", "An id identifies a unique element within the document."),
                q("Which element marks important text with strong importance?", "<strong>", "<bold>", "<emphasis>", "<important>", "strong conveys strong importance semantically."),
                q("Which element creates a numbered list?", "<ol>", "<ul>", "<dl>", "<li>", "ol represents an ordered list."),
                q("Which element defines a table row?", "<tr>", "<td>", "<th>", "<table-row>", "tr groups table cells into a row."),
                q("Which declaration identifies an HTML5 document?", "<!DOCTYPE html>", "<DOCTYPE html5>", "<!HTML>", "<html version=5>", "The HTML5 doctype is the short declaration <!DOCTYPE html>."));

        addSet(questions, "HTML", "MEDIUM",
                q("Which element groups introductory content or navigation for a page or section?", "<header>", "<head>", "<summary>", "<top>", "header represents introductory content for its nearest sectioning ancestor."),
                q("Which element is intended for a page's primary navigation links?", "<nav>", "<menuitem>", "<navigate>", "<links>", "nav represents a major navigation section."),
                q("Which element represents a self-contained composition that can stand alone?", "<article>", "<aside>", "<span>", "<main>", "article represents a self-contained composition."),
                q("Which element should identify the dominant content of a document?", "<main>", "<section>", "<footer>", "<content>", "main represents the document's dominant content."),
                q("Which attribute connects a label to a form control by ID?", "for", "target", "name", "control", "The label's for value matches the form control's id."),
                q("Which input type provides built-in email-format validation?", "email", "mail", "text-email", "address", "input type=email enables browser email validation and suitable keyboards."),
                q("Which attribute makes a form field mandatory?", "required", "needed", "validate", "must-fill", "The required attribute prevents submission when the control is empty."),
                q("Which attribute on a script element defers execution until parsing is complete?", "defer", "async-only", "delay", "late", "defer runs an external script after parsing and before DOMContentLoaded."),
                q("Which element associates a caption with a table?", "<caption>", "<summary>", "<label>", "<legend>", "caption provides a table's accessible title or description."),
                q("Which table element identifies header cells?", "<th>", "<thead-cell>", "<td-head>", "<header>", "th marks a header cell in a table."),
                q("Which attribute tells a browser to load an image lazily?", "loading=\"lazy\"", "fetch=\"later\"", "async=\"image\"", "defer=\"img\"", "loading=lazy defers loading off-screen images."),
                q("Which element provides a caption or legend for a fieldset?", "<legend>", "<labelset>", "<caption>", "<summary>", "legend labels a fieldset group."),
                q("Which element is used to embed a video with native controls?", "<video>", "<media>", "<movie>", "<source-video>", "The video element embeds video content and can expose native controls."),
                q("What is the purpose of the lang attribute on the html element?", "Identify the document's primary language", "Select a stylesheet", "Set text direction only", "Declare character encoding", "lang helps browsers and assistive technologies interpret the document language."),
                q("Which element groups related form controls with a border and caption?", "<fieldset>", "<formgroup>", "<section>", "<group>", "fieldset groups related form controls and can be named by legend."));

        addSet(questions, "HTML", "HARD",
                q("What does the defer attribute do for a classic external script?", "Executes it after parsing, preserving document order", "Executes it before parsing starts", "Makes it a module", "Prevents it from running", "Deferred classic scripts execute after parsing and in document order."),
                q("Which attribute supplies alternate image sources for responsive layouts?", "srcset", "sizeset", "media-src", "source-list", "srcset lists image candidates and their intrinsic widths or densities."),
                q("What is the purpose of the sizes attribute alongside a width-descriptor srcset?", "Describe the rendered image slot size", "Set image compression", "Declare image dimensions", "Set an alt-text language", "sizes helps the browser choose an appropriate srcset candidate."),
                q("Which relationship value identifies a stylesheet link?", "rel=\"stylesheet\"", "rel=\"style\"", "type=\"css\"", "href=\"stylesheet\"", "A link element uses rel=stylesheet to attach a stylesheet."),
                q("Which attribute gives a form control's submitted key?", "name", "id", "class", "for", "Form data is submitted as name/value pairs."),
                q("Which form method places submitted data in the request body?", "POST", "GET", "HEAD", "TRACE", "POST sends submitted form data in the request body."),
                q("What is the purpose of the sandbox attribute on an iframe?", "Restrict capabilities of embedded content", "Resize the frame automatically", "Enable cross-origin access", "Cache the frame", "sandbox applies restrictions to content in the nested browsing context."),
                q("Which attribute should be used to provide an iframe's accessible name?", "title", "alt", "label", "name only", "The iframe title describes its embedded content to assistive technology."),
                q("What does aria-describedby reference?", "An element that provides a longer description", "The element's CSS class", "A form submission URL", "A keyboard shortcut", "aria-describedby references one or more elements containing descriptive text."),
                q("Which element represents a disclosure summary for a details widget?", "<summary>", "<legend>", "<caption>", "<output>", "summary provides the visible label for its parent details element."),
                q("Which input attribute specifies a regular expression constraint?", "pattern", "regex", "validate", "match", "pattern applies a regular expression constraint to supported input types."),
                q("What does the autocomplete attribute communicate?", "Whether and how the browser may autofill a control", "Whether the field is required", "How data is encrypted", "Which endpoint receives the form", "autocomplete provides autofill hints for form controls."),
                q("Which element is designed to contain machine-readable date/time values?", "<time>", "<date>", "<clock>", "<datetime>", "time can expose a machine-readable datetime attribute."),
                q("What does the crossorigin attribute control on a script or link?", "Cross-origin request mode and credential handling", "The file's character encoding", "The page's CORS policy", "The script execution order", "crossorigin configures CORS mode and credentials for supported resource requests."),
                q("Which meta declaration helps mobile browsers size the viewport to device width?", "name=\"viewport\" content=\"width=device-width, initial-scale=1\"", "name=\"mobile\" content=\"true\"", "width=\"device\"", "content=\"responsive\"", "The viewport meta element configures the layout viewport on mobile devices."));

        addSet(questions, "CSS", "EASY",
                q("Which property sets an element's text color?", "color", "font-color", "text-color", "foreground", "color sets the foreground color of text."),
                q("Which selector targets an element with id=\"notice\"?", "#notice", ".notice", "notice", "*notice", "An ID selector begins with the # symbol."),
                q("Which selector targets elements with class=\"notice\"?", ".notice", "#notice", "notice", "@notice", "A class selector begins with a period."),
                q("Which property sets an element's background color?", "background-color", "color", "fill", "surface", "background-color sets the background color."),
                q("Which property controls the size of text?", "font-size", "text-scale", "font-weight", "line-height", "font-size sets the size of glyphs."),
                q("Which property adds space inside an element's border?", "padding", "margin", "gap", "outline", "Padding is the space between content and border."),
                q("Which property adds space outside an element's border?", "margin", "padding", "border-spacing", "inset", "Margin separates an element from neighboring boxes."),
                q("Which declaration turns an element into a flex container?", "display: flex", "position: flex", "layout: flex", "flex: display", "display:flex establishes a flex formatting context."),
                q("Which property rounds the corners of a border box?", "border-radius", "corner-style", "border-curve", "radius", "border-radius controls corner rounding."),
                q("Which property makes text bold?", "font-weight", "font-style", "text-decoration", "font-variant", "font-weight controls the thickness of text strokes."),
                q("Which property aligns inline text horizontally?", "text-align", "align-items", "justify-content", "vertical-align", "text-align controls inline content alignment within a block."),
                q("Which pseudo-class applies styles while a pointer is over an element?", ":hover", ":active", ":focus", ":visited", ":hover matches an element under a pointing device."),
                q("Which property sets the width of an element's border?", "border-width", "outline-width", "stroke-width", "edge-size", "border-width controls border thickness."),
                q("Which unit is relative to the root element's font size?", "rem", "em", "vw", "px", "rem is relative to the root element's computed font size."),
                q("Which CSS comment syntax is correct?", "/* comment */", "// comment", "<!-- comment -->", "# comment", "CSS comments use slash-star and star-slash delimiters."));

        addSet(questions, "CSS", "MEDIUM",
                q("Which property controls the main-axis alignment of flex items?", "justify-content", "align-items", "align-content", "place-items", "justify-content distributes items along the flex main axis."),
                q("Which property aligns flex items on the cross axis?", "align-items", "justify-content", "flex-direction", "order", "align-items aligns items on the cross axis."),
                q("Which declaration creates a two-dimensional grid formatting context?", "display: grid", "position: grid", "grid: display", "layout: table", "display:grid establishes a CSS Grid container."),
                q("Which box-sizing value includes padding and border in the declared width?", "border-box", "content-box", "padding-box", "margin-box", "With border-box, declared dimensions include content, padding, and border."),
                q("Which selector has the highest specificity among these?", "#panel", ".panel", "section", "*", "An ID selector has greater specificity than class, type, and universal selectors."),
                q("Which at-rule applies styles conditionally to a viewport width?", "@media", "@supports", "@container only", "@layer", "A media query applies rules based on media features such as viewport width."),
                q("Which property controls whether flex items wrap onto another line?", "flex-wrap", "flex-flow", "white-space", "overflow-wrap", "flex-wrap controls wrapping of flex items."),
                q("Which value of position anchors an element to the viewport?", "fixed", "static", "relative", "sticky only", "A fixed element is positioned relative to the viewport."),
                q("Which property sets the stacking order of positioned elements?", "z-index", "stack-order", "layer", "depth", "z-index controls stacking order in applicable stacking contexts."),
                q("What does the > combinator select?", "Direct children", "All descendants", "Adjacent siblings", "Following siblings", "The child combinator selects direct children only."),
                q("Which property defines the space between CSS Grid tracks?", "gap", "margin", "padding", "spacing", "gap sets gutters between grid or flex items."),
                q("Which property changes an element's opacity?", "opacity", "visibility", "filter-color", "alpha", "opacity sets the compositing opacity from 0 to 1."),
                q("Which property controls the speed curve of a CSS transition?", "transition-timing-function", "transition-delay", "animation-fill-mode", "transform-origin", "transition-timing-function defines the transition's easing curve."),
                q("What does minmax() do in a CSS Grid track definition?", "Sets a minimum and maximum track size", "Creates a media query", "Sets two grid areas", "Adds a gap", "minmax() defines a track's lower and upper sizing bounds."),
                q("Which unit is based on the width of the viewport?", "vw", "vh", "rem", "ch", "vw represents one percent of the viewport width."));

        addSet(questions, "CSS", "HARD",
                q("Which selector specificity tuple is typically highest?", "An ID selector", "A class selector", "A type selector", "The universal selector", "Specificity compares ID selectors above classes and type selectors."),
                q("What creates a new stacking context among these declarations?", "opacity: 0.9", "color: red", "margin: 1rem", "font-size: 1rem", "An opacity value below 1 creates a stacking context."),
                q("What is the effect of min-width: 0 on a flex item?", "Allows it to shrink below its min-content size", "Prevents it from shrinking", "Makes it invisible", "Sets its width to zero permanently", "Flex items often need min-width:0 to shrink below their automatic min-content size."),
                q("Which declaration defines a custom property?", "--brand-color: teal", "$brand-color: teal", "@property brand-color", "custom-brand-color: teal", "Custom property names begin with two hyphens and are consumed with var()."),
                q("How is a custom property value referenced?", "var(--brand-color)", "value(--brand-color)", "custom(--brand-color)", "use(--brand-color)", "The var() function reads a custom property."),
                q("What does contain: layout primarily allow the browser to do?", "Limit layout effects to an element subtree", "Disable all painting", "Force GPU compositing", "Remove an element from the DOM", "Layout containment isolates layout calculations within the element."),
                q("What does the :where() pseudo-class contribute to specificity?", "Zero specificity", "One ID specificity", "One class specificity", "The specificity of its most specific argument", ":where() and its arguments contribute zero specificity."),
                q("What does the :is() pseudo-class use for specificity?", "The most specific argument in its selector list", "Always zero", "The least specific argument", "One type selector only", ":is() takes the specificity of its most specific argument."),
                q("Which property can opt an element into a named container query context?", "container-type", "query-name", "media-type", "contain-query", "container-type establishes a query container for size queries."),
                q("What does a cascade layer primarily control?", "Priority ordering of groups of CSS rules", "DOM order", "Grid track sizing", "Font loading order", "Cascade layers provide an explicit precedence order for groups of declarations."),
                q("Which value of position lets an element scroll normally until a threshold?", "sticky", "fixed", "absolute", "static only", "Sticky positioning behaves relatively until its inset threshold is reached."),
                q("What does transform: translateZ(0) change in layout?", "It transforms the element in 3D space without changing normal flow size", "It removes the element", "It changes its intrinsic width", "It makes it fixed", "Transforms affect visual rendering but not the element's normal-flow footprint."),
                q("What does aspect-ratio: 16 / 9 establish?", "A preferred width-to-height ratio", "A fixed pixel size", "A grid with 16 columns", "A font ratio", "aspect-ratio establishes a preferred ratio used in sizing."),
                q("Which CSS function can clamp a value between a minimum and maximum?", "clamp()", "bound()", "limit()", "range()", "clamp(minimum, preferred, maximum) constrains a value."),
                q("What is the purpose of prefers-reduced-motion?", "Adapt motion effects to a user's system preference", "Detect screen size", "Set animation speed globally", "Disable JavaScript", "The media feature lets styles respect reduced-motion preferences."));

        addSet(questions, "JAVASCRIPT", "EASY",
                q("Which keyword declares a block-scoped variable that can be reassigned?", "let", "const", "var only", "static", "let declares a block-scoped reassignable binding."),
                q("Which keyword declares a binding that cannot be reassigned?", "const", "let", "fixed", "final", "const prevents reassignment of the binding."),
                q("Which symbol starts a single-line JavaScript comment?", "//", "<!--", "/* only", "#", "Two forward slashes start a single-line JavaScript comment."),
                q("Which method writes a message to the browser developer console?", "console.log()", "document.print()", "window.write()", "alert.log()", "console.log writes diagnostic output to the console."),
                q("Which operator tests strict equality without type coercion?", "===", "==", "=", "!==", "=== compares value and type without coercion."),
                q("Which value represents an intentional absence of an object value?", "null", "undefined only", "NaN", "false", "null is an explicit empty or absent object value."),
                q("Which array method adds an item to the end?", "push()", "pop()", "shift()", "slice()", "push appends one or more elements and returns the new length."),
                q("Which array method removes the final item?", "pop()", "push()", "unshift()", "map()", "pop removes and returns the last array element."),
                q("Which built-in function converts a value to a number?", "Number()", "String()", "Boolean()", "Array()", "Number() converts a value to its numeric representation when possible."),
                q("Which keyword exits a function and can provide a value?", "return", "break", "continue", "yield only", "return ends function execution and optionally returns a value."),
                q("Which syntax creates an array literal?", "[1, 2, 3]", "{1, 2, 3}", "(1, 2, 3)", "<1, 2, 3>", "Square brackets create an array literal."),
                q("Which property reports the number of items in an array?", "length", "size()", "count", "items", "An array's length property reports its number of elements."),
                q("Which event commonly fires when a user activates a button?", "click", "change only", "submit only", "load", "The click event fires when a pointer or keyboard activates a clickable element."),
                q("Which browser API selects an element by its ID using a CSS selector?", "document.querySelector('#id')", "document.getClass('#id')", "window.selectId('#id')", "document.find('#id')", "querySelector accepts a CSS selector and returns the first match."),
                q("Which value is the Boolean literal for a true condition?", "true", "'true'", "yes", "1", "true is the Boolean literal; a quoted value is a string."));

        addSet(questions, "JAVASCRIPT", "MEDIUM",
                q("Which array method returns a new array containing items that pass a test?", "filter()", "forEach()", "findIndex()", "every()", "filter creates a new array containing values whose callback returns true."),
                q("Which array method transforms each item into a new value?", "map()", "filter()", "some()", "includes()", "map returns a new array of callback results."),
                q("What does Promise.all() do when one input promise rejects?", "Rejects with that failure", "Waits forever", "Resolves with undefined", "Cancels every promise", "Promise.all rejects as soon as an input rejects."),
                q("What does the await keyword do inside an async function?", "Pauses that function until a promise settles", "Blocks the browser thread", "Converts a value to a string", "Starts a worker", "await suspends the async function and resumes with the settled value or error."),
                q("Which syntax safely reads a property that may be nullish?", "user?.name", "user!.name", "user??.name", "user::name", "Optional chaining short-circuits when the left side is null or undefined."),
                q("What does the nullish coalescing operator ?? test?", "Whether the left side is null or undefined", "Whether the left side is any falsy value", "Strict equality", "Type identity", "?? uses the fallback only for null or undefined."),
                q("Which declaration creates a function-scoped binding?", "var", "let", "const", "class", "var is function-scoped, unlike let and const which are block-scoped."),
                q("What is a closure?", "A function retaining access to its lexical scope", "A closed browser tab", "A promise without a result", "A class without methods", "A closure retains access to variables in its lexical environment."),
                q("What does Array.prototype.reduce() commonly produce?", "A single accumulated value", "Only the first matching item", "A sorted copy", "A boolean only", "reduce combines array values into an accumulated result."),
                q("Which method converts a JSON string into a JavaScript value?", "JSON.parse()", "JSON.stringify()", "Object.fromJSON()", "parseJSON()", "JSON.parse parses a JSON text string."),
                q("Which method converts a JavaScript value to JSON text?", "JSON.stringify()", "JSON.parse()", "toJSONText()", "Object.toString()", "JSON.stringify serializes a supported value into JSON text."),
                q("What does event.preventDefault() do?", "Cancels the browser's default action for the event", "Stops event propagation", "Removes the event listener", "Reloads the current page", "preventDefault cancels a cancelable event's default browser action."),
                q("What does event.stopPropagation() do?", "Stops the event from continuing through the propagation path", "Cancels the default action", "Removes all listeners", "Prevents the event from being created", "stopPropagation prevents further capture/bubble propagation."),
                q("Which operator spreads an iterable into individual values?", "...", "??", "::", "=>", "The spread syntax expands iterable elements or object properties."),
                q("What does typeof null return in JavaScript?", "object", "null", "undefined", "boolean", "typeof null returns object for historical compatibility."));

        addSet(questions, "JAVASCRIPT", "HARD",
                q("What is the result of a promise reaction handler returning a plain value?", "The next promise fulfills with that value", "The chain immediately rejects", "The handler is skipped", "The value is converted to a callback", "Promise chaining assimilates returned values as fulfillment values."),
                q("What does the JavaScript event loop coordinate?", "Execution of queued tasks and jobs around the call stack", "Java thread creation", "CSS cascade ordering", "Network routing", "The event loop schedules tasks and microtasks as the stack becomes available."),
                q("When are promise .then() callbacks generally processed?", "As microtasks after the current stack completes", "Before the current function returns", "Only after a timer task", "On a separate JavaScript thread", "Promise reactions are queued as microtasks and run after the current stack."),
                q("What is the difference between Object.freeze() and a deep freeze?", "freeze is shallow by default", "freeze recursively freezes every nested object", "freeze prevents reading properties", "freeze changes the prototype", "Object.freeze affects the object's own properties, not nested objects recursively."),
                q("What does a generator function yield?", "An iterator that can pause and resume", "A promise only", "A worker thread", "A frozen object", "Calling a generator function returns an iterator controlled by next()."),
                q("What does Symbol provide?", "A unique primitive value usable as a property key", "A globally unique string", "A class constructor", "A numeric enum only", "Each Symbol() call creates a distinct primitive value."),
                q("Which equality operation considers NaN equal to itself?", "Object.is(NaN, NaN)", "NaN === NaN", "NaN == NaN", "NaN.equals(NaN)", "Object.is treats NaN as equal to itself."),
                q("What is the purpose of AbortController in browser APIs?", "Signal cancellation to supported asynchronous operations", "Pause the JavaScript thread", "Undo a completed request", "Clear the event loop", "AbortController sends an abort signal to APIs such as fetch."),
                q("What is a common use of WeakMap?", "Associate data with object keys without keeping them alive solely by the map", "Store primitive keys", "Guarantee insertion-order iteration", "Serialize objects to JSON", "WeakMap keys are weakly held objects and entries are not enumerable."),
                q("Why does structuredClone() handle circular references?", "It uses a structured cloning algorithm that tracks visited references", "It converts every object to JSON", "It removes all object references", "It runs a recursive stringify", "Structured cloning tracks objects and supports cycles for supported values."),
                q("What does Promise.race() settle with?", "The first input promise to settle", "The first input promise to fulfill only", "All inputs in order", "The last input to settle", "Promise.race adopts the first settled input's state and value or reason."),
                q("What does Promise.any() reject with if every input rejects?", "AggregateError", "TypeError", "The first rejection reason", "undefined", "Promise.any rejects with AggregateError when no input fulfills."),
                q("What does the JavaScript module live-binding behavior mean?", "An import reflects updates to the exported binding", "An import is always a copied snapshot", "Imports can be reassigned by consumers", "Modules execute once per import statement", "ES module imports are read-only live bindings to exported values."),
                q("What does a passive event listener indicate?", "The listener will not call preventDefault for that event", "The event cannot bubble", "The listener runs in a worker", "The listener is removed after one call", "A passive listener promises not to cancel the event's default action."),
                q("What is the purpose of requestAnimationFrame()?", "Schedule work before the next repaint", "Run code after a fixed delay exactly", "Create a background thread", "Wait for a network response", "requestAnimationFrame schedules a callback before a browser repaint."));

        addSet(questions, "GENERAL_CS", "EASY",
                q("Which data structure follows first-in, first-out order?", "Queue", "Stack", "Heap", "Tree", "A queue removes items in the order they were added."),
                q("Which data structure follows last-in, first-out order?", "Stack", "Queue", "Graph", "Hash table", "A stack removes the most recently added item first."),
                q("What does CPU stand for?", "Central Processing Unit", "Computer Program Utility", "Central Power User", "Core Processing Upload", "CPU means Central Processing Unit."),
                q("Which number system uses only 0 and 1?", "Binary", "Decimal", "Octal", "Hexadecimal", "Binary is a base-2 number system."),
                q("How many bits are in one byte?", "8", "4", "16", "10", "A byte contains eight bits."),
                q("Which protocol secures HTTP communication using TLS?", "HTTPS", "FTP", "SMTP", "Telnet", "HTTPS is HTTP protected by TLS."),
                q("What does RAM primarily provide to a running computer?", "Temporary working memory", "Permanent archival storage", "A network connection", "Power regulation", "RAM holds data and instructions currently in use."),
                q("Which component executes program instructions?", "Processor", "Monitor", "Keyboard", "Power supply", "The processor executes machine instructions."),
                q("What is an operating system's main role?", "Manage hardware resources and provide services to programs", "Compile every website", "Replace all applications", "Encrypt every file by default", "An OS manages resources and provides common system services."),
                q("Which device is primarily used to input typed text?", "Keyboard", "Monitor", "Speaker", "Projector", "A keyboard is a common text input device."),
                q("What does URL stand for?", "Uniform Resource Locator", "Universal Routing Link", "Unified Record List", "User Reference Location", "URL means Uniform Resource Locator."),
                q("Which storage type typically retains data without power?", "SSD", "RAM", "CPU cache", "Register", "An SSD is non-volatile storage."),
                q("What does LAN describe?", "A local area network", "A language access node", "A long-distance antenna", "A link allocation number", "LAN means Local Area Network."),
                q("Which unit is commonly used for processor clock frequency?", "Hertz", "Byte", "Pixel", "Volt-hour", "Hertz measures cycles per second."),
                q("What does a web browser primarily do?", "Request and display web resources", "Compile Java source code", "Route packets between networks", "Replace the operating system", "A browser retrieves web resources and renders documents."));

        addSet(questions, "GENERAL_CS", "MEDIUM",
                q("What is the average lookup complexity of a well-distributed hash table?", "O(1)", "O(n)", "O(log n)", "O(n log n)", "Hash table lookup is expected O(1) with a suitable hash distribution."),
                q("Which traversal visits a binary search tree in sorted key order?", "In-order", "Pre-order", "Post-order", "Level-order only", "In-order traversal of a binary search tree visits keys in sorted order."),
                q("What is the time complexity of binary search on a sorted array?", "O(log n)", "O(n)", "O(n log n)", "O(1) always", "Binary search halves the remaining search interval each step."),
                q("Which algorithmic technique solves a problem by combining smaller subproblems?", "Divide and conquer", "Linear probing", "Round robin", "Memo deletion", "Divide and conquer splits a problem, solves subproblems, and combines results."),
                q("What does an IP address identify at the network layer?", "A network interface endpoint", "A web page title", "A file format", "A CPU instruction", "An IP address identifies a host interface for network communication."),
                q("Which transport protocol provides ordered, reliable byte-stream delivery?", "TCP", "UDP", "ARP", "DNS", "TCP provides reliable, ordered byte-stream transport."),
                q("What is a primary purpose of DNS?", "Map domain names to resource records such as IP addresses", "Encrypt local files", "Assign CPU schedules", "Compress images", "DNS resolves names using distributed resource records."),
                q("What does normalization in relational database design reduce?", "Redundant data and update anomalies", "Network latency", "CPU instruction count", "HTML payload size", "Normalization organizes relations to reduce redundancy and anomalies."),
                q("Which SQL command retrieves rows from a table?", "SELECT", "INSERT", "UPDATE", "DROP", "SELECT queries data from one or more tables."),
                q("What is a primary key?", "A column or column set that uniquely identifies each row", "A password stored in a table", "Any nullable column", "A duplicate index", "A primary key uniquely identifies rows and cannot be null."),
                q("What does an operating system scheduler decide?", "Which runnable task gets CPU time", "Which monitor displays a window", "Which URL is valid", "Which file extension is legal", "The scheduler allocates CPU time among runnable tasks."),
                q("What does a cache exploit?", "Temporal and spatial locality", "Random access only", "Network encryption", "Lossless compression only", "Caches benefit when recently or nearby accessed data is likely to be reused."),
                q("What is an API?", "A defined interface for software components to communicate", "A processor architecture", "A storage medium", "An encryption algorithm", "An API specifies operations and data through which software components interact."),
                q("Which version-control operation records a repository snapshot?", "Commit", "Fetch", "Checkout only", "Ignore", "A commit records a snapshot of staged changes."),
                q("What does Big-O notation describe?", "Asymptotic growth of resource use", "Exact runtime in seconds", "Code readability", "Memory addresses", "Big-O describes an upper-bound style growth rate as input size increases."));

        addSet(questions, "GENERAL_CS", "HARD",
                q("Which condition is sufficient for a deadlock in the Coffman model?", "Mutual exclusion, hold-and-wait, no preemption, and circular wait", "High CPU utilization alone", "A full disk", "Two processes with no shared resources", "The four Coffman conditions together characterize the possibility of deadlock."),
                q("What does a semaphore primarily control?", "Access to a finite count of shared resources", "DNS resolution", "Heap compaction", "File compression", "A semaphore maintains permits that coordinate concurrent access."),
                q("What is the key property of a serializable database schedule?", "Its result is equivalent to some serial execution", "Every transaction runs alone", "It never uses locks", "It always has maximum throughput", "Serializability ensures equivalence to a serial ordering of transactions."),
                q("What does ACID atomicity guarantee?", "A transaction's changes happen completely or not at all", "Every query is fast", "Data is always replicated", "Transactions run without conflicts", "Atomicity prevents partial transaction effects from being committed."),
                q("What is the CAP theorem trade-off considered during a network partition?", "Consistency versus availability", "CPU versus memory", "Latency versus compression", "Authentication versus authorization", "During a partition, a distributed system cannot guarantee both consistency and availability in the CAP sense."),
                q("What is a Merkle tree useful for?", "Efficiently verify data integrity using hashes", "Schedule processes", "Allocate IP addresses", "Compress source code", "Merkle trees summarize data with hashes and support efficient inclusion proofs."),
                q("What does a public-key digital signature provide?", "Integrity and signer authenticity", "Confidentiality by itself", "Guaranteed availability", "Data compression", "A signature can verify integrity and possession of the signing private key."),
                q("What is the main goal of asymmetric encryption in key exchange?", "Establish or protect a secret without pre-sharing it", "Make hashing reversible", "Avoid all certificates", "Compress the network stream", "Public-key techniques can establish shared secrets over an untrusted channel."),
                q("What does a Bloom filter trade for space efficiency?", "It may report false positives but not false negatives", "It stores exact values", "It sorts all values", "It guarantees unique input", "A standard Bloom filter can produce false positives, not false negatives."),
                q("What does amortized analysis measure?", "Average cost per operation over a sequence", "Worst latency of one network packet", "Only best-case complexity", "Exact runtime on one machine", "Amortized analysis bounds average cost across a sequence of operations."),
                q("What is the purpose of a write-ahead log?", "Record changes before applying them to durable data pages", "Store browser cookies", "Cache DNS queries", "Compile SQL to HTML", "WAL supports recovery by persisting log records before data-page updates."),
                q("What is a zero-day vulnerability?", "A vulnerability unknown to or unpatched by the responsible vendor", "A bug fixed before release", "A scheduled maintenance window", "A password that expires today", "Zero-day describes a vulnerability with no available vendor fix at disclosure or exploitation time."),
                q("What is the difference between authentication and authorization?", "Identity verification versus permission checking", "Encryption versus hashing", "Backup versus recovery", "Compilation versus interpretation", "Authentication establishes who a user is; authorization determines permitted actions."),
                q("What does eventual consistency allow?", "Replicas may temporarily differ but converge if updates stop", "Reads are always linearizable", "Writes are never replicated", "Transactions are always serial", "Eventual consistency permits temporary replica divergence before convergence."),
                q("Which approach helps prevent SQL injection in database queries?", "Parameterized queries", "String concatenation with escaping only", "Hiding the database name", "Changing column names", "Parameterized statements keep query structure separate from user-provided values."));

        return List.copyOf(questions);
    }

    private static QuestionSeed q(String question, String correct, String distractorOne,
                                  String distractorTwo, String distractorThree, String explanation) {
        return new QuestionSeed(question, correct, distractorOne, distractorTwo, distractorThree, explanation);
    }

    private static void addSet(List<Question> questions, String category, String difficulty, QuestionSeed... seeds) {
        for (QuestionSeed seed : seeds) {
            List<String> options = new java.util.ArrayList<>(List.of(
                    seed.correctAnswer(), seed.distractorOne(), seed.distractorTwo(), seed.distractorThree()));
            java.util.Collections.shuffle(options);
            questions.add(new Question(questions.size() + 1, category, difficulty, seed.question(),
                    options, seed.correctAnswer(), seed.explanation()));
        }
    }

    private record QuestionSeed(String question, String correctAnswer, String distractorOne,
                                String distractorTwo, String distractorThree, String explanation) {
    }

    public List<Question> getAllQuestions() {
        return QUESTIONS;
    }

        public int getQuestionsPerQuiz() {
                return QUESTIONS_PER_QUIZ;
        }

    public List<String> getCategories() {
        return QUESTIONS.stream()
                .map(Question::getCategory)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    public List<QuizCategoryResponse> getCategoryResponses() {
        return List.of(
                new QuizCategoryResponse("JAVA", "Java"),
                new QuizCategoryResponse("HTML", "HTML"),
                new QuizCategoryResponse("CSS", "CSS"),
                new QuizCategoryResponse("JAVASCRIPT", "JavaScript"),
                new QuizCategoryResponse("GENERAL_CS", "General Computer Science")
        );
    }

    public List<Question> getQuestions(String category, String difficulty) {
        String normalizedCategory = category == null ? null : category.trim();
        String normalizedDifficulty = difficulty == null ? null : difficulty.trim();

        return QUESTIONS.stream()
                .filter(question -> normalizedCategory == null || normalizedCategory.isBlank()
                        || question.getCategory().equalsIgnoreCase(normalizedCategory))
                .filter(question -> normalizedDifficulty == null || normalizedDifficulty.isBlank()
                        || question.getDifficulty().equalsIgnoreCase(normalizedDifficulty))
                .sorted(Comparator.comparingInt(Question::getId))
                .toList();
    }

    public Question findQuestionById(int questionId) {
        return QUESTIONS.stream()
                .filter(question -> question.getId() == questionId)
                .findFirst()
                .orElse(null);
    }
}
