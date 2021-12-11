package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.Model.Feed;
import com.cinemates20.Model.Movie;
import com.cinemates20.Model.MovieList;
import com.cinemates20.Model.Notification;
import com.cinemates20.Model.Review;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.MovieCardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;

public class GenericAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<T> list;
    private final Context context;
    private static final int NOTIFICATION_REQUEST_ACCEPTED = 1;
    private static final int NOTIFICATION_REQUEST_RECEIVED = 2;
    private static final int NOTIFICATION_REPORT = 16;
    private static final int GENRE = 3;
    private static final int LIST_MOVIE_NAME = 4;
    private static final int COMMENT = 5;
    private static final int ALL_REVIEW = 7;
    private static final int PERSONAL_REVIEW = 8;
    private static final int CAST = 9;
    private static final int FRIEND = 10;
    private static final int FEED_REVIEW = 11;
    private static final int FEED_COMMENT = 12;
    private static final int FEED_FRIENDSHIP = 13;
    private static final int FEED_VALUATION = 14;
    private static final int FEED_MOVIE_LIST = 15;
    //interface for click item
    private ClickListener clickListener;

    public GenericAdapter(List<T> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof Notification){
            if(((Notification) list.get(position)).getTypeNotification().equals("RequestAccepted"))
                return NOTIFICATION_REQUEST_ACCEPTED;
            else if(((Notification) list.get(position)).getTypeNotification().equals("RequestReceived"))
                return NOTIFICATION_REQUEST_RECEIVED;
            else return NOTIFICATION_REPORT;
        }
        else if(list.get(position) instanceof Genre)
            return GENRE;
        else if(list.get(position) instanceof MovieList)
            return LIST_MOVIE_NAME;
        else if(list.get(position) instanceof Comment)
            return COMMENT;
        else if(list.get(position) instanceof Review){
            Fragment activeFragment = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
            if(activeFragment instanceof MovieCardFragment)
                return ALL_REVIEW;
            else
                return PERSONAL_REVIEW;
        }
        else if(list.get(position) instanceof PersonCast)
            return CAST;
        else if(list.get(position) instanceof String)
            return FRIEND;
        else if(list.get(position) instanceof Feed){
            switch (((Feed) list.get(position)).getItemNewsType()) {
                case "review":
                    return FEED_REVIEW;
                case "comment":
                    return FEED_COMMENT;
                case "friendship":
                    return FEED_FRIENDSHIP;
                case "valuation":
                    return FEED_VALUATION;
                case "movieList":
                    return FEED_MOVIE_LIST;
                default:
                    return 0;
            }
        }
        else return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case NOTIFICATION_REQUEST_ACCEPTED:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_NotificationAccepted(v);
            case NOTIFICATION_REQUEST_RECEIVED:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_friend_request, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_NotificationReceived(v);
            case NOTIFICATION_REPORT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_NotificationAccepted(v);
            case GENRE:
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100));
                return new ViewHolder_Genre(v);
            case LIST_MOVIE_NAME:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_lists_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_List(v);
            case COMMENT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_user_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_Comment(v);
            case ALL_REVIEW:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_user_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_AllReview(v);
            case PERSONAL_REVIEW:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_review_user_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_PersonalReview(v);
            case CAST:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_Cast(v);
            case FRIEND:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_Friends(v);
            case FEED_REVIEW:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_review_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_FeedWithPoster(v);
            case FEED_COMMENT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_FeedComment(v);
            case FEED_FRIENDSHIP:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_friendship_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_FeedFriendship(v);
            case FEED_VALUATION:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_review_valuation, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_FeedValuation(v);
            case FEED_MOVIE_LIST:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_review_row, parent, false);
                v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolder_FeedWithPoster(v);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        switch (itemType){
            case NOTIFICATION_REQUEST_ACCEPTED:
                String userWhoSentRequest = ((Notification)list.get(position)).getUserWhoSent();

                Uri uri = userDAO.getImageUri(userWhoSentRequest);

                String time = Utils.setTime(((Notification)list.get(position)).getDateAndTime().getTime());

                ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Now you and <b>" + userWhoSentRequest + "</b> are friends."));
                ((ViewHolder_NotificationAccepted) holder).getTxtTimeAndDate().setText(time);
                if (!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_NotificationAccepted) holder).getIcon());
                break;

            case NOTIFICATION_REQUEST_RECEIVED:
                userWhoSentRequest = ((Notification)list.get(position)).getUserWhoSent();

                uri = userDAO.getImageUri(userWhoSentRequest);

                time = Utils.setTime(((Notification)list.get(position)).getDateAndTime().getTime());

                ((ViewHolder_NotificationReceived)holder).getTitle().setText(userWhoSentRequest);
                ((ViewHolder_NotificationReceived)holder).getTxtTimeAndDate().setText(time);
                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_NotificationReceived)holder).getIcon());
                ((ViewHolder_NotificationReceived)holder).getButtonConfirm().setOnClickListener(view -> clickListener.onItemClickListener(userWhoSentRequest, "confirm", position));
                ((ViewHolder_NotificationReceived)holder).getButtonDelete().setOnClickListener(view -> clickListener.onItemClickListener(userWhoSentRequest, "delete", position));
                break;

            case NOTIFICATION_REPORT:
                String typeUser = ((Notification) list.get(position)).getTypeUser();
                String typeItem = ((Notification)list.get(position)).getTypeItem();
                boolean resultReport = ((Notification) list.get(position)).isResultReport();

                time = Utils.setTime(((Notification)list.get(position)).getDateAndTime().getTime());
                ((ViewHolder_NotificationAccepted) holder).getTxtTimeAndDate().setText(time);

                if(resultReport){ // report accepted by admin
                    if(typeUser.equals("author")) {
                        if(typeItem.equals("review"))
                            ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Your review has been removed for inappropriate language"));
                        else
                            ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Your comment has been removed for inappropriate language"));
                    } else
                        ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Your report has been approved by the administrator."));
                } else{
                    if(typeUser.equals("author")) {
                        if(typeItem.equals("review"))
                            ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Your review is visible again"));
                        else
                            ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Your comment is visible again"));
                    } else
                        ((ViewHolder_NotificationAccepted) holder).getTextNotification().setText(Html.fromHtml("Your report has been rejected by the administrator."));
                }

                ((ViewHolder_NotificationAccepted) holder).getIcon().setImageResource(R.drawable.report);
                break;

            case GENRE:
                ((ViewHolder_Genre)holder).getGenreName().setText(((Genre)list.get(position)).getName());
                break;

            case LIST_MOVIE_NAME:
                MovieList movieList = ((MovieList)list.get(position));

                ((ViewHolder_List)holder).nameListView.setText(movieList.getNameList());
                ((ViewHolder_List)holder).seeMovieListImage.setOnClickListener(view -> clickListener.onItemClickListener(movieList));
                ((ViewHolder_List)holder).itemView.setOnClickListener(view -> clickListener.onItemClickListener(movieList));
                break;

            case COMMENT:
                Comment comment = ((Comment)list.get(position));
                ((ViewHolder_Comment)holder).getAuthorComment().setText(comment.getAuthor());

                uri = userDAO.getImageUri(comment.getAuthor());
                if(!uri.toString().equals("")){
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_Comment) holder).getIconUser());
                }

                if(comment.getCounterForSpoiler() >= 3){
                    ((ViewHolder_Comment) holder).getTxtComment().setVisibility(View.GONE);
                    ((ViewHolder_Comment) holder).getOption().setVisibility(View.GONE);
                    ((ViewHolder_Comment) holder).getTxtTimeAndDate().setVisibility(View.GONE);
                    ((ViewHolder_Comment) holder).getButtonViewSpoiler().setVisibility(View.VISIBLE);
                    ((ViewHolder_Comment) holder).getTxtSpoiler().setVisibility(View.VISIBLE);

                    ((ViewHolder_Comment) holder).getButtonViewSpoiler().setOnClickListener(view -> {
                        ((ViewHolder_Comment) holder).getTxtComment().setVisibility(View.VISIBLE);
                        ((ViewHolder_Comment) holder).getOption().setVisibility(View.VISIBLE);
                        ((ViewHolder_Comment) holder).getTxtTimeAndDate().setVisibility(View.VISIBLE);
                        ((ViewHolder_Comment) holder).getButtonViewSpoiler().setVisibility(View.GONE);
                        ((ViewHolder_Comment) holder).getTxtSpoiler().setVisibility(View.GONE);
                    });
                }

                ((ViewHolder_Comment) holder).getTxtComment().setText(comment.getTextComment());

                time = Utils.setTime(comment.getDateAndTime().getTime());
                ((ViewHolder_Comment) holder).getTxtTimeAndDate().setText(time);

                //if currentUser is the author of comment then he can't report himself
                String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
                if(comment.getAuthor().equals(currentUser))
                    ((ViewHolder_Comment) holder).getOption().setVisibility(View.GONE);

                ((ViewHolder_Comment) holder).getOption().setOnClickListener(view -> clickListener.onItemClickListener(comment));
                break;

            case ALL_REVIEW:
                Review authorReview = (Review) list.get(position);

                ((ViewHolder_AllReview)holder).getAuthorName().setText(authorReview.getAuthor());

                uri = userDAO.getImageUri(authorReview.getAuthor());
                if(!uri.toString().equals("")){
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_AllReview)holder).getAuthorIcon());
                }

                if(authorReview.getCounterForSpoiler() >= 3)
                    ((ViewHolder_AllReview) holder).getSpoilerText().setVisibility(View.VISIBLE);

                Uri finalUri = uri;
                holder.itemView.setOnClickListener(view ->
                        clickListener.onItemClickListener(authorReview, finalUri.toString(), ((ViewHolder_AllReview)holder).getAuthorIcon(), ((ViewHolder_AllReview)holder).getAuthorName()));

                ((ViewHolder_AllReview)holder).getSeeReviewButton().setOnClickListener(view ->
                        clickListener.onItemClickListener(authorReview, finalUri.toString(), ((ViewHolder_AllReview)holder).getAuthorIcon(), ((ViewHolder_AllReview)holder).getAuthorName()));
                break;

            case PERSONAL_REVIEW:
                Review review = (Review) list.get(position);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
                    Movie movie = movieDAO.getMovieById(review.getIdMovie());
                    handler.post(() -> {
                        ((ViewHolder_PersonalReview)holder).getMovieTitle().setText(movie.getTitle());

                        Glide.with(context)
                                .load("http://image.tmdb.org/t/p/original" + movie.getPosterPath())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(200, 300)
                                .transform(new RoundedCorners(30))
                                .into(((ViewHolder_PersonalReview)holder).getMoviePoster());
                    });
                });

                if(!review.getTextReview().equals(""))
                    ((ViewHolder_PersonalReview)holder).getSeePersonalReview().setVisibility(View.VISIBLE);
                else
                    ((ViewHolder_PersonalReview)holder).getSeePersonalReview().setVisibility(View.INVISIBLE);

                ((ViewHolder_PersonalReview)holder).getValuation().setRating(review.getMovieValuation());

                if(!review.getTextReview().equals(""))
                    holder.itemView.setOnClickListener(view ->
                            clickListener.onItemClickListener(review));

                ((ViewHolder_PersonalReview)holder).getSeePersonalReview().setOnClickListener(view ->
                        clickListener.onItemClickListener(review));
                break;

            case CAST:
                Person person = (Person) list.get(position);
                if(!person.getProfilePath().equals("")) {
                    Glide.with(context.getApplicationContext())
                            .load("http://image.tmdb.org/t/p/w300" + person.getProfilePath())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_Cast) holder).getImagePerson());

                    ((ViewHolder_Cast)holder).getNamePerson().setText(person.getName());
                }
                break;

            case FRIEND:
                String friend = (String) list.get(position);

                uri = userDAO.getImageUri(friend);

                ((ViewHolder_Friends)holder).getName().setText(friend);
                ((ViewHolder_Friends)holder).getButton().setText("Remove");
                ((ViewHolder_Friends)holder).getButton().setBackgroundColor(ContextCompat.getColor(context, R.color.dark_gray));

                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_Friends) holder).getIcon());

                ((ViewHolder_Friends) holder).getButton().setOnClickListener(view -> {
                    Toast.makeText(view.getContext(), "Friendship with " + friend + " canceled",
                            Toast.LENGTH_SHORT).show();
                    clickListener.onItemClickListener(friend, position);
                    ((ViewHolder_Friends) holder).buttonState = "notFriend";
                });
                break;

            case FEED_REVIEW:
                Feed feed = (Feed) list.get(position);

                //get image of user of the news
                uri = userDAO.getImageUri(feed.getUserOfTheNews());
                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_FeedWithPoster) holder).getUserIcon());

                time = Utils.setTime(feed.getDateAndTime().getTime());
                ((ViewHolder_FeedWithPoster) holder).getDate().setText(time);

                /*get movie info (name, poster, ecc) by id of movie*/
                executor = Executors.newSingleThreadExecutor();
                handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
                    Movie movie = movieDAO.getMovieById(Integer.parseInt(feed.getMovie()));
                    handler.post(() -> {
                        //set the type of the news
                        ((ViewHolder_FeedWithPoster) holder).getUsername().setText(Html.fromHtml("<b>" + feed.getUserOfTheNews()
                                + "</b> has reviewed <b>" + movie.getTitle() + "</b>"));
                        Glide.with(context)
                                .load("http://image.tmdb.org/t/p/original" + movie.getPosterPath())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(800, 900)
                                .transform(new RoundedCorners(30))
                                .into(((ViewHolder_FeedWithPoster)holder).getMoviePoster());
                    });
                });

                /*on click item*/
                Uri finalUri1 = uri;
                holder.itemView.setOnClickListener(view -> clickListener.onItemClickListener(feed, finalUri1.toString(), null));
                break;

            case FEED_COMMENT:
                feed = (Feed) list.get(position);

                uri = userDAO.getImageUri(feed.getUserOfTheNews());
                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_FeedComment) holder).getUserIcon());

                ((ViewHolder_FeedComment) holder).getTextView().setText(Html
                        .fromHtml("<b>" + feed.getUserOfTheNews() + "</b> has commented a review of <b>"
                                + feed.getSecondUser() + "</b>."));

                time = Utils.setTime(feed.getDateAndTime().getTime());
                ((ViewHolder_FeedComment) holder).getDate().setText(time);

                /*on click item*/
                Uri finalUri2 = uri;
                holder.itemView.setOnClickListener(view -> clickListener.onItemClickListener(feed, finalUri2.toString(), null));
                break;

            case FEED_FRIENDSHIP:
                feed = (Feed) list.get(position);

                /* First user icon */
                uri = userDAO.getImageUri(feed.getUserOfTheNews());
                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_FeedFriendship) holder).getFirstUserIcon());

                /* Second user icon */
                Uri secondUri = userDAO.getImageUri(feed.getSecondUser());
                if(!secondUri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(secondUri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_FeedFriendship) holder).getSecondUserIcon());

                ((ViewHolder_FeedFriendship) holder).getTextView().setText(Html.fromHtml("Now <b> " +
                        feed.getUserOfTheNews() + "</b> and <b>" + feed.getSecondUser() + "</b> are friends."));

                time = Utils.setTime(feed.getDateAndTime().getTime());
                ((ViewHolder_FeedFriendship) holder).getDate().setText(time);
                break;

            case FEED_VALUATION:
                feed = (Feed) list.get(position);

                uri = userDAO.getImageUri(feed.getUserOfTheNews());
                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_FeedValuation) holder).getFirstUserIcon());

                time = Utils.setTime(feed.getDateAndTime().getTime());
                ((ViewHolder_FeedValuation) holder).getDate().setText(time);

                /*get movie info (name, poster, ecc) by id of movie*/
                executor = Executors.newSingleThreadExecutor();
                handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
                    Movie movie = movieDAO.getMovieById(Integer.parseInt(feed.getMovie()));
                    handler.post(() -> {
                        ((ViewHolder_FeedValuation) holder).getTextView().setText(Html.fromHtml("<b>" + feed.getUserOfTheNews() +
                                "</b> has valuated <b>" + movie.getTitle() + "</b> with rating"));

                        ((ViewHolder_FeedValuation) holder).getValuation().setRating(feed.getValuation());

                        holder.itemView.setOnClickListener(view -> clickListener.onItemClickListener(feed, "", movie));
                    });
                });
                break;

            case FEED_MOVIE_LIST:
                feed = (Feed) list.get(position);

                uri = userDAO.getImageUri(feed.getUserOfTheNews());
                if(!uri.toString().equals(""))
                    Glide.with(context.getApplicationContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CircleCrop())
                            .into(((ViewHolder_FeedWithPoster) holder).getUserIcon());

                /*get movie info (name, poster, ecc) by id of movie*/
                executor = Executors.newSingleThreadExecutor();
                handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
                    Movie movie = movieDAO.getMovieById(Integer.parseInt(feed.getMovie()));
                    handler.post(() -> {
                        //set the type of the news
                        ((ViewHolder_FeedWithPoster) holder).getUsername().setText(Html.fromHtml(feed.getUserOfTheNews() + " has reviewed <b>" + movie.getTitle() + "</b>"));
                        Glide.with(context)
                                .load("http://image.tmdb.org/t/p/original" + movie.getPosterPath())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(800, 900)
                                .transform(new RoundedCorners(30))
                                .into(((ViewHolder_FeedWithPoster)holder).getMoviePoster());

                        ((ViewHolder_FeedWithPoster) holder).getUsername().setText(Html
                                .fromHtml("<b>" + feed.getUserOfTheNews() + "</b> has added <b>"
                                        + movie.getTitle() + "</b> into a list."));
                    });
                });



                time = Utils.setTime(feed.getDateAndTime().getTime());
                ((ViewHolder_FeedWithPoster) holder).getDate().setText(time);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder_Genre extends RecyclerView.ViewHolder {

        private final TextView genreName;

        public ViewHolder_Genre(@NonNull View itemView) {
            super(itemView);
            genreName = itemView.findViewById(R.id.genreName);
        }

        public TextView getGenreName() {
            return genreName;
        }
    }

    public static class ViewHolder_NotificationAccepted extends RecyclerView.ViewHolder {

        private final TextView textNotification, txtTimeAndDate;
        private final ImageView icon;

        public ViewHolder_NotificationAccepted(@NonNull View itemView) {
            super(itemView);
            textNotification = itemView.findViewById(R.id.txtNotification);
            icon = itemView.findViewById(R.id.userPropic);
            txtTimeAndDate = itemView.findViewById(R.id.date);
        }

        public ImageView getIcon() {
            return icon;
        }

        public TextView getTextNotification() {
            return textNotification;
        }

        public TextView getTxtTimeAndDate() {
            return txtTimeAndDate;
        }
    }

    public static class ViewHolder_NotificationReceived extends RecyclerView.ViewHolder {

        private final TextView title, txtTimeAndDate;
        private final Button buttonConfirm, buttonDelete;
        private final ImageView icon;

        public ViewHolder_NotificationReceived(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtName);
            txtTimeAndDate = itemView.findViewById(R.id.time);
            buttonConfirm = itemView.findViewById(R.id.buttonAccetta);
            buttonDelete = itemView.findViewById(R.id.buttonRifiuta);
            icon = itemView.findViewById(R.id.imageView8);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getTxtTimeAndDate() {
            return txtTimeAndDate;
        }

        public Button getButtonConfirm() {
            return buttonConfirm;
        }

        public Button getButtonDelete() {
            return buttonDelete;
        }

        public ImageView getIcon() {
            return icon;
        }
    }

    public static class ViewHolder_List extends RecyclerView.ViewHolder {

        private final TextView nameListView;
        private final ImageView seeMovieListImage;

        public ViewHolder_List(View v) {
            super(v);
            nameListView = v.findViewById(R.id.txtNameList);
            seeMovieListImage = v.findViewById(R.id.seeList);
        }

        public ImageView getSeeMovieListImage() {
            return seeMovieListImage;
        }

        public TextView getNameListView() {
            return nameListView;
        }
    }

    public static class ViewHolder_Comment extends RecyclerView.ViewHolder{

        private final TextView authorComment, txtComment, txtTimeAndDate, txtSpoiler, buttonViewSpoiler;
        private final ImageView iconUser, option;

        public ViewHolder_Comment(@NonNull View itemView) {
            super(itemView);
            authorComment = itemView.findViewById(R.id.txtName);
            txtComment = itemView.findViewById(R.id.txtComment);
            txtTimeAndDate = itemView.findViewById(R.id.txtDateComment);
            iconUser = itemView.findViewById(R.id.userPropic);
            option = itemView.findViewById(R.id.option);
            txtSpoiler = itemView.findViewById(R.id.textView5);
            buttonViewSpoiler = itemView.findViewById(R.id.button3);
        }

        public TextView getAuthorComment() {
            return authorComment;
        }

        public TextView getTxtComment() {
            return txtComment;
        }

        public TextView getTxtTimeAndDate() {
            return txtTimeAndDate;
        }

        public ImageView getIconUser() {
            return iconUser;
        }

        public ImageView getOption() {
            return option;
        }

        public TextView getTxtSpoiler() {
            return txtSpoiler;
        }

        public TextView getButtonViewSpoiler() {
            return buttonViewSpoiler;
        }
    }

    public static class ViewHolder_AllReview extends RecyclerView.ViewHolder{

        private final TextView authorName, spoilerText;
        private final ImageView authorIcon;
        private final FloatingActionButton seeReviewButton;

        public ViewHolder_AllReview(@NonNull View itemView) {
            super(itemView);

            authorName = itemView.findViewById(R.id.txtName);
            seeReviewButton = itemView.findViewById(R.id.imageView5);
            authorIcon = itemView.findViewById(R.id.imageView7);
            spoilerText = itemView.findViewById(R.id.spoilerWarning);
        }

        public TextView getAuthorName() {
            return authorName;
        }

        public TextView getSpoilerText() {
            return spoilerText;
        }

        public ImageView getAuthorIcon() {
            return authorIcon;
        }

        public FloatingActionButton getSeeReviewButton() {
            return seeReviewButton;
        }
    }

    public static class ViewHolder_PersonalReview extends RecyclerView.ViewHolder{

        private final ImageView moviePoster;
        private final FloatingActionButton seePersonalReview;
        private final TextView movieTitle;
        private final RatingBar valuation;

        public ViewHolder_PersonalReview(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.txtTitleMovieReview);
            moviePoster = itemView.findViewById(R.id.moviePosterReview);
            seePersonalReview = itemView.findViewById(R.id.seePersonalReviewButton);
            valuation = itemView.findViewById(R.id.ratingBar);
        }

        public ImageView getMoviePoster() {
            return moviePoster;
        }

        public FloatingActionButton getSeePersonalReview() {
            return seePersonalReview;
        }

        public TextView getMovieTitle() {
            return movieTitle;
        }

        public RatingBar getValuation() {
            return valuation;
        }
    }

    public static class ViewHolder_Cast extends RecyclerView.ViewHolder{

        private final ImageView imagePerson;
        private final TextView namePerson;

        public ViewHolder_Cast(@NonNull View itemView) {
            super(itemView);
            imagePerson = itemView.findViewById(R.id.castImg);
            namePerson = itemView.findViewById(R.id.personTextView);
        }

        public ImageView getImagePerson() {
            return imagePerson;
        }

        public TextView getNamePerson() {
            return namePerson;
        }
    }

    public static class ViewHolder_Friends extends RecyclerView.ViewHolder{
        private final TextView name;
        private final Button button;
        private final ImageView icon;
        private String buttonState = "friend";

        public ViewHolder_Friends(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtName);
            button = itemView.findViewById(R.id.button);
            icon = itemView.findViewById(R.id.imageView6);
        }

        public TextView getName() {
            return name;
        }

        public Button getButton() {
            return button;
        }

        public ImageView getIcon() {
            return icon;
        }
    }

    public static class ViewHolder_FeedWithPoster extends RecyclerView.ViewHolder{

        private final TextView username, date;
        private final ImageView userIcon, moviePoster;

        public ViewHolder_FeedWithPoster(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.txtName);
            date = itemView.findViewById(R.id.date);
            userIcon = itemView.findViewById(R.id.userPropic);
            moviePoster = itemView.findViewById(R.id.posterMovieFeed);
        }

        public TextView getUsername() {
            return username;
        }

        public TextView getDate() {
            return date;
        }

        public ImageView getUserIcon() {
            return userIcon;
        }

        public ImageView getMoviePoster() {
            return moviePoster;
        }

    }

    public static class ViewHolder_FeedFriendship extends RecyclerView.ViewHolder{

        private final TextView textView, date;
        private final ImageView firstUserIcon, secondUserIcon;

        public ViewHolder_FeedFriendship(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txtName);
            date = itemView.findViewById(R.id.date);
            firstUserIcon = itemView.findViewById(R.id.userPropic);
            secondUserIcon = itemView.findViewById(R.id.user2Propic);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getDate() {
            return date;
        }

        public ImageView getFirstUserIcon() {
            return firstUserIcon;
        }

        public ImageView getSecondUserIcon() {
            return secondUserIcon;
        }
    }

    public static class ViewHolder_FeedValuation extends RecyclerView.ViewHolder{

        private final TextView textView, date;
        private final ImageView firstUserIcon;
        private final RatingBar valuation;

        public ViewHolder_FeedValuation(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txtName);
            date = itemView.findViewById(R.id.date);
            firstUserIcon = itemView.findViewById(R.id.userPropic);
            valuation = itemView.findViewById(R.id.ratingBar2);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getDate() {
            return date;
        }

        public ImageView getFirstUserIcon() {
            return firstUserIcon;
        }

        public RatingBar getValuation() {
            return valuation;
        }
    }

    public static class ViewHolder_FeedComment extends RecyclerView.ViewHolder{

        private final TextView textView, date;
        private final ImageView userIcon;

        public ViewHolder_FeedComment(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txtNotification);
            date = itemView.findViewById(R.id.date);
            userIcon = itemView.findViewById(R.id.userPropic);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getDate() {
            return date;
        }

        public ImageView getUserIcon() {
            return userIcon;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        default void onItemClickListener(String userWhoSentRequest, String buttonType, int position) {}
        default void onItemClickListener(Comment commentSelected) {}
        default void onItemClickListener(MovieList listClicked) {}
        default void onItemClickListener(Review personalReviewClicked) {}
        default void onItemClickListener(Feed object, String iconAuthor, Movie movieDb) {}
        default void onItemClickListener(Review review, String iconAuthor, ImageView authorIcon, TextView authorName) {}
        default void onItemClickListener(String friend, int position) {}
    }
}
